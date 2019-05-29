package top.imlgw.spike.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import top.imlgw.spike.vo.GoodsVo;

import java.util.List;

/**
 * @author imlgw.top
 * @date 2019/5/23 14:28
 */
@Mapper
@Component
public interface GoodsDao {

    @Select("select g.*,sg.stock_count, sg.start_date, sg.end_date,sg.spike_price from spike_goods sg left join goods g on sg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*,sg.stock_count, sg.start_date, sg.end_date,sg.spike_price from spike_goods sg inner join goods g on sg.goods_id = g.id and sg.goods_id=#{id}")
    GoodsVo getGoodsVoById(long id);

    @Insert("update spike_goods set stock_count = stock_count-1 where goods_id=#{goodsId}")
    int reduceStock(long goodsId);
}
