package top.imlgw.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.intercept.NeedLogin;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.SpikeService;
import top.imlgw.spike.vo.GoodsVo;
import top.imlgw.spike.vo.OrderDetailVo;

/**
 * @author imlgw.top
 * @date 2019/5/25 19:18
 */
@Controller
@RequestMapping("/spike")
public class SpikeController {

    @Autowired
    private SpikeService spikeService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 没优化前：5000*10 QPS 950左右
     * @param model
     * @param spikeUser
     * @param goodsId
     * @return 秒杀接口
     */
    @RequestMapping("/do_spike")
    @NeedLogin
    public String do_spike(Model model, SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
        if (spikeUser==null) { //没有登录
            return "login";
        }
        //检查库存
        GoodsVo goodsVo= goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goodsVo.getStockCount(); //这里拿的秒杀商品里面的库存,不是商品里面的库存
        if(stock<=0){
            model.addAttribute("failMsg",CodeMsg.STOCK_EMPTY);
            return "spike_fail";
        }
        //看是否重复秒杀
        SpikeOrder spikeOrder=spikeService.getGoodsVoByUserIdAndGoodsId(spikeUser.getId(), goodsId);
        if(spikeOrder!=null){
            model.addAttribute("failMsg",CodeMsg.SPIKE_REPEAT);
            return "spike_fail";
        }
        OrderInfo orderInfo=spikeService.doSpike(spikeUser.getId(),goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order_detail";
    }


    /**前后端分离，页面静态化
     * @param spikeUser
     * @param goodsId
     * @return
     */
    @RequestMapping("/spike")
    @ResponseBody
    @NeedLogin
    public Result<OrderInfo> do_spike1(SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
        if (spikeUser==null) { //没有登录
            return Result.error(CodeMsg.LOGIN_NONE);
        }
        //检查库存
        GoodsVo goodsVo= goodsService.getGoodsVoByGoodsId(goodsId);

        int stock=goodsVo.getStockCount(); //这里拿的秒杀商品里面的库存,不是商品里面的库存
        if(stock<=0){
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        //看是否重复秒杀
        SpikeOrder spikeOrder=spikeService.getGoodsVoByUserIdAndGoodsId(spikeUser.getId(), goodsId);
        if(spikeOrder!=null){
            return Result.error(CodeMsg.SPIKE_REPEAT);
        }
        OrderInfo orderInfo=spikeService.doSpike(spikeUser.getId(),goodsVo);
        return Result.success(orderInfo);
    }
}
