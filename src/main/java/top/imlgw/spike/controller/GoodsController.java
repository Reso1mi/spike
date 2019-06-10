package top.imlgw.spike.controller;

/**
 * @author imlgw.top
 * @date 2019/5/15 17:31
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.redis.GoodsKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.vo.GoodsDetailVo;
import top.imlgw.spike.vo.GoodsVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    //private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SpikeUserService spikeUserService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 未做缓存前：5000*10并发，QPS大概 1000
     * 缓存后：5000 * 10的并发下 QPS提高到了3000左右
     * @param model
     * @param spikeUser
     * @param request
     * @param response
     * @return redis 页面缓存
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String tolist(Model model, SpikeUser spikeUser,
                         HttpServletRequest request, HttpServletResponse response) {

        String html = redisService.get(GoodsKey.glsPrefix, "goodsList", String.class);
        if (!StringUtils.isEmpty(html)) {
            //logger.info("从redis缓存中获取页面信息");
            return html;
        }
        List<GoodsVo> goodsVos = goodsService.goodsVoList();
        model.addAttribute("user", spikeUser);
        model.addAttribute("goodsList", goodsVos);

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        String goods_list = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        redisService.set(GoodsKey.glsPrefix, "goodsList", goods_list);
        //logger.info("从mysql中获取");
        return goods_list;
    }


    /**
     * @param response
     * @param model
     * @param cookie
     * @param param
     * @return 未采用 webconfigrur 从参数中获取token
     */
    @Deprecated
    @RequestMapping("/to_list0")
    public String tolist0(HttpServletResponse response, Model model,
                          @CookieValue(value = SpikeUserService.COOK_NAME_TOKEN, required = false) String cookie,
                          @RequestParam(value = SpikeUserService.COOK_NAME_TOKEN, required = false) String param) {
        //手机浏览器，有可能将cookie放在参数中
        System.out.println("cookie:" + cookie);
        System.out.println("param " + param);
        if (param == null && cookie == null) {
            return "login";
        }
        String cook = cookie != null ? cookie : param;
        SpikeUser user = spikeUserService.getUserByToken(response, cook);
        System.out.println(user);
        model.addAttribute("user", user);
        return "goods_list";
    }


    /**
     * 前后端分离后台接口
     * @param spikeUser
     * @param id
     * @return 页面静态化，直接将数据缓存到浏览器中
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetailStatic(SpikeUser spikeUser, @PathVariable("goodsId") long id) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(id);
        long now=System.currentTimeMillis();
        long startTime=goodsVo.getStartDate().getTime();
        long endTime=goodsVo.getEndDate().getTime();

        int remainSeconds=0;
        if(now<startTime){ //未开始
            remainSeconds=(int) ((startTime-now)/1000);
        }else if(now>endTime){ //已经结束
            remainSeconds = -1;
        }else {
            //正在进行中
            remainSeconds=0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo(spikeUser,goodsVo,remainSeconds);
        return Result.success(goodsDetailVo);
    }


    /**
     * @param model
     * @param spikeUser
     * @param id
     * @param request
     * @param response
     * @return URL缓存商品详情信息 60s过期时间
     */
    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail1(Model model, SpikeUser spikeUser, @PathVariable("goodsId") long id,
                           HttpServletRequest request, HttpServletResponse response) {
        //先从Redis中获取页面数据 ---URL缓存
        String html = redisService.get(GoodsKey.gdsPrefix, "" + id, String.class);
        if (!StringUtils.isEmpty(html)) {
            //logger.info("从redis缓存中获取商品详情");
            return html;
        }
        model.addAttribute("user", spikeUser);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goodsVo);
        //查询秒杀时间的信息
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //秒杀状态
        int spikeStatus = 0;
        int remainTime = 0;
        if (now < startAt) { //秒杀未开始
            spikeStatus = 0;
            remainTime = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            spikeStatus = 2;
            remainTime = -1;
        } else {//秒杀进行中
            spikeStatus = 1;
            remainTime = 0;
        }
        model.addAttribute("remainTime", remainTime);
        model.addAttribute("spikeStatus", spikeStatus);

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        String goods_detail= thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        redisService.set(GoodsKey.gdsPrefix, ""+id, goods_detail);
        //logger.info("从mysql获取商品详情信息");
        return goods_detail;
    }


    @RequestMapping("/to_detail0/{goodsId}")
    public String toDetail0(Model model, SpikeUser spikeUser, @PathVariable("goodsId") long id) {
        model.addAttribute("user", spikeUser);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goodsVo);

        //查询秒杀时间的信息
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int spikeStatus = 0;
        int remainTime = 0;
        if (now < startAt) { //秒杀未开始
            spikeStatus = 0;
            remainTime = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            spikeStatus = 2;
            remainTime = -1;
        } else {//秒杀进行中
            spikeStatus = 1;
            remainTime = 0;
        }
        model.addAttribute("remainTime", remainTime);
        model.addAttribute("spikeStatus", spikeStatus);
        return "goods_detail";
    }
}

