package top.imlgw.spike.vo;

import top.imlgw.spike.entity.SpikeUser;

/**
 * @author imlgw.top
 * @date 2019/6/7 22:09
 */
public class GoodsDetailVo {
    private SpikeUser user;
    private GoodsVo goods;
    private int remainSeconds = 0;

    public GoodsDetailVo(SpikeUser user, GoodsVo goods, int remainSeconds) {
        this.user = user;
        this.goods = goods;
        this.remainSeconds = remainSeconds;
    }

    public SpikeUser getUser() {
        return user;
    }

    public void setUser(SpikeUser user) {
        this.user = user;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
