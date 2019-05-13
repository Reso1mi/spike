package top.imlgw.spike.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.utils.MD5Util;
import top.imlgw.spike.utils.ValidatorUtil;
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

    @Autowired
    private SpikeUserService spikeUserService;

    @RequestMapping("/to_register")
    public String to_register(){
        return "register";
    }

    @RequestMapping("/do_register")
    @ResponseBody
    public Result<CodeMsg> doRegister(RegisterVo vo){
        System.out.println(vo);
        //二次校验信息 表单校验
        String mobile = vo.getMobile();
        String password = vo.getPassword();
        String nickname=vo.getNickname();
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(StringUtils.isEmpty(nickname)){
            return Result.error(CodeMsg.NICKNAME_EMPTY);
        }
        if(StringUtils.isEmpty(password)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //注册
        if(register(vo)){
            return Result.success(CodeMsg.REGISTER_SUCCESS);
        }
        return  Result.error(CodeMsg.SERVER_ERROR);
    }

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
    }

}
