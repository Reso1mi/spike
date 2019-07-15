package top.imlgw.spike.controller;

/**
 * @author imlgw.top
 * @date 2019/5/15 17:31
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.vo.GoodsDetailVo;
import top.imlgw.spike.vo.GoodsVo;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger logger=LoggerFactory.getLogger(GoodsController.class);

    private static final String GOODS_LIST_KEY="goods_list";

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;


    /**
     * 未做缓存前：5000*10并发，QPS大概 1000
     * 页面缓存后：5000*10的并发下 QPS提高到了3000左右
     * 前后端完全分离后：
     * @return
     */
    @RequestMapping(value = "/get_list")
    @ResponseBody
    public Result<List<GoodsVo>> tolist() {
        List<GoodsVo> redisVoList = redisService.get(GoodsKey.glsPrefix, GOODS_LIST_KEY, List.class);
        if (redisVoList!=null&&redisVoList.size()>0) {
            logger.info("从redis缓存中获取商品列表的信息");
            return Result.success(redisVoList);
        }
        List<GoodsVo> voList = goodsService.goodsVoList();
        //上下循序不能交换
        redisService.set(GoodsKey.glsPrefix, GOODS_LIST_KEY, voList);
        logger.info("从mysql中获取商品列表的信息");
        System.out.println(voList);
        return Result.success(voList);
    }

    /**
     * 前后端分离后台接口
     *
     * @param spikeUser
     * @param id
     * @return 页面静态化，直接将数据缓存到浏览器中
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetailStatic(SpikeUser spikeUser, @PathVariable("goodsId") long id) {

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(id);
        long now = System.currentTimeMillis();
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();

        int remainSeconds = 0;
        if (now < startTime) { //未开始
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) { //已经结束
            remainSeconds = -1;
        } else {
            //正在进行中
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo(spikeUser, goodsVo, remainSeconds);
        return Result.success(goodsDetailVo);
    }
}

