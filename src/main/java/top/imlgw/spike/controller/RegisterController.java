package top.imlgw.spike.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.utils.MD5Util;
import top.imlgw.spike.vo.RegisterVo;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;

import java.util.Date;

/**
 * @author imlgw.top
 * @date 2019/5/13 12:55
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    private static Logger logger=LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SpikeUserService spikeUserService;


    @RequestMapping("/do_register")
    @ResponseBody
    public Result<CodeMsg> doRegister(@Validated @RequestBody RegisterVo vo){
        logger.info(vo.toString());
        //注册
        spikeUserService.register(vo);
        return  Result.success(CodeMsg.REGISTER_SUCCESS);
    }

    @Deprecated
    public boolean register(RegisterVo vo){
        //构建这个用户
        SpikeUser user = new SpikeUser();
        user.setId(Long.parseLong(vo.getMobile()));
        user.setNickname(vo.getNickname());
        //生成随机的盐
        String dbSalt= MD5Util.randomSalt(6);
        //将表单加密后的password加盐后再加密
        String dbPass= MD5Util.formPassToDBPass(vo.getPassword(), dbSalt);
        System.out.println(dbPass);
        user.setPassword(dbPass);
        user.setSalt(dbSalt);
        user.setRegisterDate(new Date());
        return spikeUserService.insert(user);
    } //放到业务层

}
