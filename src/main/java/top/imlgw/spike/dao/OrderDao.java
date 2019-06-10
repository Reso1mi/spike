package top.imlgw.spike.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;

/**
 * @author imlgw.top
 * @date 2019/5/26 13:50
 */
@Mapper
@Component
public interface OrderDao {

    @Select("select * from order_info where id=#{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);

    /**
     * @param spikeUserId
     * @param goodsId
     * @return 是否已经秒杀过
     */
    @Select("select * from spike_order where user_id=#{spikeUserId} and goods_id=#{goodsId}")
    SpikeOrder getOrderByUserIdAndGoodsId(@Param("spikeUserId") long spikeUserId,@Param("goodsId") long goodsId);


    /**
     * @param orderInfo
     * @return 插入订单 ,返回主键
     */
    @Insert("insert into order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date,pay_date) " +
            "values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate},#{payDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    int insertToOrder(OrderInfo orderInfo);


    /**
     * @param spikeOrder
     * @return 插入秒杀订单(成功秒杀)
     */
    @Insert("insert into spike_order (user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId})")
    int insertToSpikeOrder(SpikeOrder spikeOrder);
}
