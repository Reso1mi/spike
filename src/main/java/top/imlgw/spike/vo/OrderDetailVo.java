package top.imlgw.spike.vo;

import top.imlgw.spike.entity.OrderInfo;

/**
 * @author imlgw.top
 * @date 2019/6/7 22:52
 */
public class OrderDetailVo {
    private OrderInfo orderInfo;
    private GoodsVo goodsVo;

    public OrderDetailVo(OrderInfo orderInfo, GoodsVo goodsVo) {
        this.orderInfo = orderInfo;
        this.goodsVo = goodsVo;
    }

    public OrderDetailVo() {
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }
}
