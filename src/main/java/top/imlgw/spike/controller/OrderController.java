package top.imlgw.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.OrderInfo;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.intercept.NeedLogin;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.OrderService;
import top.imlgw.spike.vo.GoodsVo;
import top.imlgw.spike.vo.OrderDetailVo;

/**
 * @author imlgw.top
 * @date 2019/6/8 22:13
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    @NeedLogin
    public Result<OrderDetailVo> getOrderDetail(SpikeUser spikeUser,@RequestParam("orderId")long orderId){
        /*拦截器中判断
        if(spikeUser==null){
            return Result.error(CodeMsg.SERVER_ERROR);
        }*/
        OrderInfo orderInfo = orderService.getOrderInfoById(orderId);
        if(orderInfo==null){
            return Result.error(CodeMsg.ORDRER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo(orderInfo, goodsVo);
        return Result.success(orderDetailVo);
    }
}
