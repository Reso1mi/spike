package top.imlgw.spike.entity;

/**
 * @author imlgw.top
 * @date 2019/5/23 14:20
 */
public class SpikeOrder {
    private Long id;
    private Long userId;
    private Long  orderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    private Long goodsId;

}
