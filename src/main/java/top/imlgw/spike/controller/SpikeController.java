package top.imlgw.spike.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.intercept.NeedLogin;
import top.imlgw.spike.rabbitmq.MQSender;
import top.imlgw.spike.rabbitmq.SpikeMessage;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.SpikeService;
import top.imlgw.spike.vo.GoodsVo;

import java.util.List;

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
     * 没优化前：5000*10 QPS 950左右
     * @param spikeUser
     * @param goodsId
     * @return 秒杀接口
     * 前后端分离，页面静态化
     */
    @RequestMapping("/do_spike")
    @ResponseBody
    @NeedLogin
    public Result<Integer> do_spike(SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
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

        //预减库存
        Long stock= redisService.decr(GoodsKey.spikeGoodsStock, "" + goodsId);
        if (stock<0){
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        //判断是否已经秒杀到
        SpikeOrder spikeOrder= spikeService.getGoodsVoByUserIdAndGoodsId(spikeUser.getId(), goodsId);
        if(spikeOrder!=null){
            return Result.error(CodeMsg.SPIKE_REPEAT);
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
}
