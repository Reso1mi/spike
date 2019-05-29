package top.imlgw.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;
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

    public SpikeOrder getGoodsVoByUserIdAndGoodsId(long spikeUserId, long goodsId){
        return orderService.getOrderByUserIdAndGoodsId(spikeUserId,goodsId);
    }


    /**
     * @param userId
     * @param goodsVo
     * @return 秒杀，提供事务
     */
    @Transactional
    public OrderInfo doSpike(long userId,GoodsVo goodsVo) {
        //减少库存
        goodsService.reduceStock(goodsVo.getId());
        //生成订单
        return orderService.createOrder(userId,goodsVo);
    }
}
