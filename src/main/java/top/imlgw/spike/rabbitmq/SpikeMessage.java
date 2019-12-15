package top.imlgw.spike.rabbitmq;

import top.imlgw.spike.entity.SpikeUser;

/**
 * @author imlgw.top
 * @date 2019/12/15 15:24
 */
public class SpikeMessage {
    private SpikeUser user;
    private long goodsId;
    public SpikeUser getUser() {
        return user;
    }
    public void setUser(SpikeUser user) {
        this.user = user;
    }
    public long getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
