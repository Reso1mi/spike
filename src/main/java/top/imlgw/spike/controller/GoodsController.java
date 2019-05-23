package top.imlgw.spike.controller;

/**
 * @author imlgw.top
 * @date 2019/5/15 17:31
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.redis.SpikeUserKey;
import top.imlgw.spike.service.SpikeUserService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private RedisService service;

    @Autowired
    private SpikeUserService spikeUserService;


    @RequestMapping("/to_list")
    public String tolist(Model model,SpikeUser spikeUser) {
        if (spikeUser==null) {
            return "login";
        }
        model.addAttribute("user", spikeUser);
        return "goods_list";
    }

    @SuppressWarnings("all")
    @RequestMapping("/to_list0")
    @Deprecated
    public String tolist0(HttpServletResponse response, Model model,
                         @CookieValue(value = SpikeUserService.COOK1_NAME_TOKEN, required = false) String cookie,
                         @RequestParam(value = SpikeUserService.COOK1_NAME_TOKEN, required = false) String param) {
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




    @SuppressWarnings("all")
    @RequestMapping("/to_detail")
    public String toDetail(HttpServletResponse response, Model model,
                         @CookieValue(value = SpikeUserService.COOK1_NAME_TOKEN, required = false) String cookie,
                         @RequestParam(value = SpikeUserService.COOK1_NAME_TOKEN, required = false) String param) {
        /*手机浏览器，有可能将cookie放在参数中*/
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

}

