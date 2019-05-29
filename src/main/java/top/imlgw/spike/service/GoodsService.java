package top.imlgw.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imlgw.spike.dao.GoodsDao;
import top.imlgw.spike.vo.GoodsVo;

import java.util.List;

/**
 * @author imlgw.top
 * @date 2019/5/23 17:16
 */
@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> goodsVoList(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long id) {
        return goodsDao.getGoodsVoById(id);
    }

    public void reduceStock(Long id) {
        goodsDao.reduceStock(id);
    }
}
