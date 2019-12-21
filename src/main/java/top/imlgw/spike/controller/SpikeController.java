package top.imlgw.spike.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.imlgw.spike.access.AccessLimit;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.intercept.NeedLogin;
import top.imlgw.spike.rabbitmq.MQSender;
import top.imlgw.spike.rabbitmq.SpikeMessage;
import top.imlgw.spike.redis.AccessKey;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.SpikeService;
import top.imlgw.spike.vo.GoodsVo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author imlgw.top
 * @date 2019/5/25 19:18
 */
@Controller
@RequestMapping("/spike")
public class SpikeController implements InitializingBean {

    private static Logger logger=LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private SpikeService spikeService;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    /**
     * 创建一个map作为内存标记，key为秒杀商品id，value表示是否秒杀结束，减少redis访问
     */
    private Map<Long, Boolean> localOverMap = new HashMap<>();


    /**
     * 没优化前：5000*10 QPS 950左右
     * 优化后：5000*10 QPS 1300左右
     * @param spikeUser
     * @param goodsId
     * @return 秒杀接口
     * 前后端分离，页面静态化
     */
    @RequestMapping(value = "/{path}/do_spike", method = RequestMethod.POST)
    @ResponseBody
    @NeedLogin
    public Result<Integer> do_spike(SpikeUser spikeUser, @RequestParam("goodsId") long goodsId,@PathVariable("path") String path) {
        //检查库存
        /*GoodsVo goodsVo= goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goodsVo.getStockCount(); //这里拿的是秒杀商品里面的库存,不是商品里面的库存
        if(stock<0){
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        //看是否已经秒杀到,防止重复下单
        SpikeOrder spikeOrder=spikeService.getGoodsVoByUserIdAndGoodsId(spikeUser.getId(), goodsId);
        if(spikeOrder!=null){
            return Result.error(CodeMsg.SPIKE_REPEAT);
        }
        OrderInfo orderInfo=spikeService.doSpike(spikeUser.getId(),goodsVo);
        return Result.success(orderInfo);*/


        //从Redis中取出path验证是否正确
        boolean isValid = spikeService.checkPath(spikeUser.getId(),goodsId,path);
        if(!isValid){ //路径错误
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        if (localOverMap.get(goodsId)){ //JVM缓存
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        //判断是否已经秒杀到
        SpikeOrder spikeOrder= spikeService.getGoodsVoByUserIdAndGoodsId(spikeUser.getId(), goodsId);
        if(spikeOrder!=null){
            return Result.error(CodeMsg.SPIKE_REPEAT);
        }

        //Redis预减库存
        Long stock= redisService.decr(GoodsKey.spikeGoodsStock, "" + goodsId);
        if (stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        SpikeMessage spikeMessage=new SpikeMessage();
        spikeMessage.setGoodsId(goodsId);
        spikeMessage.setUser(spikeUser);
        //mq发送消息
        mqSender.sendSpikeMessage(spikeMessage);
        return Result.success(0); //排队中
    }

    /**
     * 系统初始化,将库存提前加载到Redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.goodsVoList();
        if (goodsVos==null){
            return;
        }
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.spikeGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        }
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> secKillResult( SpikeUser spikeUser,
                                       @RequestParam("goodsId") Long goodsId) {
        if (spikeUser== null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = spikeService.getSpikeResult(spikeUser.getId(), goodsId);
        logger.info("客户端轮询");
        return Result.success(result);
    }

    /**
     * @param spikeUser
     * @param goodsId
     * @param verifyCode
     * @param request
     * @return 请求秒杀路径
     */
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    @NeedLogin
    @AccessLimit(second = 5,maxCount = 5) //自定义注解,实现限流操作
    public Result<String> secKillPath(SpikeUser spikeUser, @RequestParam("goodsId") Long goodsId,
                                      @RequestParam(value = "verifyCode",defaultValue ="0")Integer verifyCode,
                                      HttpServletRequest request) {
        if (spikeUser== null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //手工限流 5s访问5次
        /*String requestURI = request.getRequestURI();
        String key=requestURI+"_"+spikeUser.getId();
        Integer accessCount= redisService.get(AccessKey.access, key, Integer.class);
        if (accessCount==null){
            redisService.set(AccessKey.access,key,1);
        }else if (accessCount<5){
            redisService.incr(AccessKey.access,key);
        }else {
            return Result.error(CodeMsg.ACCESS_LIMIT);
        }*/
        //校验验证码
        boolean isVerifyCodeRight = spikeService.checkVerifyCode(spikeUser.getId(),goodsId,verifyCode);
        if(!isVerifyCodeRight){
            return  Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
        //根据用户id和商品id生成path
        String path = spikeService.createSecKillPath(spikeUser,goodsId);
        return Result.success(path); //将路径返回给客户端
    }

    /**
     * 在秒杀进行中，用户需要输入验证码才能进行秒杀，如果商品没有在秒杀，就不要验证码，在goods_detail.htm中，
     * countDown()会判断商品当前是否正在秒杀中，正在秒杀的那个case就会请求verifyCode接口，生成验证码，返回并渲染到页面
     * secKillService中生成的验证码，会以前缀+用户id+商品id为key,验证码为value存入redis,以供/path接口中验证使用
     * @param secKillUser
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<BufferedImage> verifyCode(HttpServletResponse response, SpikeUser secKillUser, @RequestParam("goodsId")
            Long goodsId, @RequestParam(value = "timestamp",required = false)String timestamp) {
        logger.info("/verifyCode-----timestamp："+ timestamp);
        if (secKillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage bufferedImage = spikeService.createVerifyCode(secKillUser.getId(),goodsId);
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage,"JPEG",outputStream);
            outputStream.flush();
            outputStream.close();
            //验证码已通过ImageIO写到前端
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
    }
}
