package top.imlgw.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imlgw.spike.dao.OrderDao;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.OrderKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.vo.GoodsVo;

import java.util.Date;

/**
 * @author imlgw.top
 * @date 2019/5/26 13:58
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisService redisService;

    /**
     * @param orderId
     * @return 获取order信息
     */
    public OrderInfo getOrderInfoById(long orderId){
        return  orderDao.getOrderById(orderId);
    }

    /**
     * @param goodsId
     * @return 从Redis中断是否重复秒杀
     */
    public SpikeOrder getOrderByUserIdAndGoodsId(long userId, long goodsId){
        //return orderDao.getOrderByUserIdAndGoodsId(spikeUserId,goodsId); 从缓存中取得
        return redisService.get(OrderKey.getSpikeOrderByUidGid,""+userId+"_"+goodsId,SpikeOrder.class);
    }

    /**
     * @param userId
     * @param goodsVo
     * @return 创建订单并插入对应的表中（事务）
     */
    @Transactional
    public OrderInfo createOrder(long userId, GoodsVo goodsVo) {
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsPrice(goodsVo.getSpikePrice());
        //商品数量
        orderInfo.setGoodsCount(1);
        //订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
        orderInfo.setStatus(0);
        orderDao.insertToOrder(orderInfo); //将主键封装到bean中了
        SpikeOrder spikeOrder = new SpikeOrder();
        spikeOrder.setOrderId(orderInfo.getId());
        spikeOrder.setUserId(userId);
        spikeOrder.setGoodsId(goodsVo.getId());
        //key: prefix+userId_goodsId  value: spikeOrder
        //将订单信息存到Redis中
        redisService.set(OrderKey.getSpikeOrderByUidGid,""+userId+"_"+goodsVo.getId(),spikeOrder);
        orderDao.insertToSpikeOrder(spikeOrder);
        return orderInfo;
    }
}
