package top.imlgw.spike.vo;

import top.imlgw.spike.entity.Goods;

import java.util.Date;

/**
 * @author imlgw.top
 * @date 2019/5/23 14:31
 */
public class GoodsVo  extends Goods {
    private Double spikePrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public Double getSpikePrice() {
        return spikePrice;
    }

    public void setSpikePrice(Double spikePrice) {
        this.spikePrice = spikePrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
