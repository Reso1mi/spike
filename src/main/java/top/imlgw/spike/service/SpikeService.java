package top.imlgw.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.vo.GoodsVo;

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
}
