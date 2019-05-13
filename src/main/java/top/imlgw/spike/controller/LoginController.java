package top.imlgw.spike.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.utils.MD5Util;
import top.imlgw.spike.utils.ValidatorUtil;
import top.imlgw.spike.vo.LoginVo;



/**
 * @author imlgw.top
 * @date 2019/5/12 15:57
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger=LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SpikeUserService spikeUserService;

    @RequestMapping("/to_login")
    public String login(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<CodeMsg> doLogin(LoginVo vo){
        logger.info(vo.toString());
        //表单校验
        String mobile = vo.getMobile();
        String password = vo.getPassword();
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(StringUtils.isEmpty(password)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //登陆
        CodeMsg codeMsg=login(vo);
        if(codeMsg.getCode()==0){
            return Result.success(codeMsg);
        }
        return Result.error(codeMsg);
    }

    public CodeMsg login(LoginVo vo){
        if(vo==null){
            return CodeMsg.SERVER_ERROR;
        }
        //看手机号是否存在
        SpikeUser user= spikeUserService.getById(Long.parseLong(vo.getMobile()));
        if(user==null){
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        //db密码
        String dbPass = user.getPassword();
        //拿到db盐
        String dbSalt=user.getSalt();
        //表单提交过来的密码转换为db加密的格式然后验证是不是相等
        String formPass = MD5Util.formPassToDBPass(vo.getPassword(), dbSalt);
        if(formPass.equals(dbPass)){
            return CodeMsg.SUCCESS;
        }
        return CodeMsg.PASSWORD_ERROR;
    }
}
