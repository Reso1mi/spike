package top.imlgw.spike.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.redis.SpikeKey;
import top.imlgw.spike.utils.MD5Util;
import top.imlgw.spike.utils.UUIDUtil;
import top.imlgw.spike.utils.VerifyCodeUtil;
import top.imlgw.spike.vo.GoodsVo;
import top.imlgw.spike.vo.VerifyCodeVo;
import java.awt.image.BufferedImage;

/**
 * @author imlgw.top
 * @date 2019/5/26 13:02
 */
@Service
public class SpikeService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    public SpikeOrder getGoodsVoByUserIdAndGoodsId(long spikeUserId, long goodsId){
        return orderService.getOrderByUserIdAndGoodsId(spikeUserId,goodsId);
    }


    /**
     * @param userId
     * @param goodsVo
     * @return 秒杀事务
     */
    @Transactional
    public OrderInfo doSpike(long userId,GoodsVo goodsVo) {
        //减少库存
        if(goodsService.reduceStock(goodsVo.getId())){
            //生成订单
            return orderService.createOrder(userId,goodsVo);
        }
        setGoodsSecKillOver(goodsVo.getId());
        return null;
    }


    /**
     * @param userId
     * @param goodsId
     * @return orderId 秒杀成功
     *          0      排队,继续轮询
     *          -1     秒杀失败
     */
    public long getSpikeResult(Long userId, Long goodsId) {
        SpikeOrder secKillOrder = orderService.getOrderByUserIdAndGoodsId(userId, goodsId);
        //成功
        if(secKillOrder != null){
            return  secKillOrder.getOrderId();
        }else{
            boolean isSecKillOver = getGoodsSecKillOver(goodsId);
            return isSecKillOver?-1:0;
        }
    }

    /**
     * 获取商品秒杀状态
     * @param goodsId
     * @return
     */
    private boolean getGoodsSecKillOver(Long goodsId) {
        return redisService.exists(GoodsKey.isGoodsSecKillOver,""+goodsId);
    }

    /**
     * 设置商品秒杀状态
     * @param goodsId
     */
    private void setGoodsSecKillOver(Long goodsId) {
        redisService.set(GoodsKey.isGoodsSecKillOver,"" + goodsId,true);
    }

    public boolean checkPath(Long secKillUserId, Long goodsId, String path) {
        if(secKillUserId == null || goodsId == null || path == null){
            return false;
        }
        String redisPath = redisService.get(SpikeKey.getSecKillPath, "" + secKillUserId + "_" + goodsId, String.class);
        return path.equals(redisPath);
    }

    public String createSecKillPath(SpikeUser secKillUser, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.getUuid());
        //秒杀地址存入redis
        redisService.set(SpikeKey.getSecKillPath,""+secKillUser.getId()+"_"+goodsId,str);
        return str;
    }

    public BufferedImage createVerifyCode(Long secKillUserId, Long goodsId) {
        if(secKillUserId == null || goodsId == null ){
            return null;
        }
        VerifyCodeVo verifyCodeVo = VerifyCodeUtil.createVerifyCode();

        //把验证码放入redis
        Integer result = verifyCodeVo.getResult();
        redisService.set(SpikeKey.getSecKillVerifyCode,secKillUserId+"_"+goodsId,result);
        //返回bufferImage到Controller
        return verifyCodeVo.getBufferedImage();

    }

    public boolean checkVerifyCode(Long secKillUserId, Long goodsId, Integer verifyCode) {
        if(secKillUserId == null || goodsId == null || verifyCode == null){
            return false;
        }
        Integer redisVerifyCode = redisService.get(SpikeKey.getSecKillVerifyCode, secKillUserId + "_" + goodsId, Integer.class);
        if(redisVerifyCode == null || redisVerifyCode - verifyCode != 0){
            return false;
        }
        redisService.del(SpikeKey.getSecKillVerifyCode,secKillUserId + "_" + goodsId);
        return true;
    }
}
