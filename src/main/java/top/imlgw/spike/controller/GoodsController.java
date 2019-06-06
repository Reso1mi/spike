package top.imlgw.spike.controller;

/**
 * @author imlgw.top
 * @date 2019/5/15 17:31
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.vo.GoodsVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private SpikeUserService spikeUserService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/to_list")
    public String tolist(Model model,SpikeUser spikeUser) {
        List<GoodsVo> goodsVos = goodsService.goodsVoList();
        model.addAttribute("user", spikeUser);
        model.addAttribute("goodsList",goodsVos);
        return "goods_list";
    }



    /*
    *
    * */
    @Deprecated
    @SuppressWarnings("all")
    @RequestMapping("/to_list0")
    public String tolist0(HttpServletResponse response, Model model,
                         @CookieValue(value = SpikeUserService.COOK_NAME_TOKEN, required = false) String cookie,
                         @RequestParam(value = SpikeUserService.COOK_NAME_TOKEN, required = false) String param) {
                            //手机浏览器，有可能将cookie放在参数中
        System.out.println("cookie:" +cookie);
        System.out.println("param " +param);
        if (param == null && cookie == null) {
            return "login";
        }
        String cook = cookie != null ? cookie : param;
        SpikeUser user = spikeUserService.getUserByToken(response, cook);
        System.out.println(user);
        model.addAttribute("user", user);
        return "goods_list";
    }



    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model,SpikeUser spikeUser,@PathVariable("goodsId") long id) {
        if (spikeUser==null) {
            return "login";
        }
        model.addAttribute("user", spikeUser);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goodsVo);

        //查询秒杀时间的信息
        long startAt=goodsVo.getStartDate().getTime();
        long endAt=goodsVo.getEndDate().getTime();
        long now=System.currentTimeMillis();

        int spikeStatus=0;
        int remainTime=0;
        if(now<startAt){ //秒杀未开始
            spikeStatus=0;
            remainTime= (int) ((startAt-now)/1000);
        }else if(now>endAt){//秒杀已经结束
            spikeStatus=2;
            remainTime=-1;
        }else {//秒杀进行中
            spikeStatus=1;
            remainTime=0;
        }
        model.addAttribute("remainTime",remainTime);
        model.addAttribute("spikeStatus",spikeStatus);
        return "goods_detail";
    }


}

