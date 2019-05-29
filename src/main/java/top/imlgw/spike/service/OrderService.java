package top.imlgw.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imlgw.spike.dao.OrderDao;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;
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

    public SpikeOrder getOrderByUserIdAndGoodsId(long spikeUserId, long goodsId){
        return orderDao.getOrderByUserIdAndGoodsId(spikeUserId,goodsId);
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
        orderDao.insertToSpikeOrder(spikeOrder);
        return orderInfo;
    }
}
