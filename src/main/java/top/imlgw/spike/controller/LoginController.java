package top.imlgw.spike.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.utils.MD5Util;
import top.imlgw.spike.vo.LoginVo;
import javax.servlet.http.HttpServletResponse;


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

    @RequestMapping(value = "/do_login",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response,@Validated @RequestBody LoginVo vo){
        //表单校验 ---采用JSR校验
        logger.info(vo.toString());
        //改进，采用异常来处理，代码更加简洁
        spikeUserService.login(response,vo);
        return Result.success(true);
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
    } //放到业务层
}
