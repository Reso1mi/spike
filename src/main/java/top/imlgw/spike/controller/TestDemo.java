package top.imlgw.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.dao.UserDao;
import top.imlgw.spike.entity.User;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.vo.LoginVo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;


/**
 * @author imlgw.top
 * @date 2019/5/11 14:18
 */
@Controller
@Validated
public class TestDemo {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    /*
    *框架测试
    * */
    @RequestMapping("/demo")
    public String test(ModelMap model){
        model.addAttribute("name","imlgw.top");
        return  "test";
    }

/*    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("Hello world");
    }*/

    /*
    *Redis 测试
    * */
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet(){
        /*Long res= redisService.get("www.imlgw.top", Long.class);
        return Result.success(res);*/
        return null;
    }


/*    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        //boolean set = redisService.set("www.imlgw.top", "999999");
        return Result.success(false);
    }*/

    /*
     *封装RedisKey
     * */
/*    @RequestMapping("/redis/test")
    @ResponseBody
    public Result<User> redisGetSet(){
        User user0 =new User(3,"imlgw",20,"110");
        boolean set = redisService.set(UserKey.userId ,"lgw",user0);
        User user = redisService.get(UserKey.userId ,"lgw", User.class);
        return Result.success(user);
    }*/

    @RequestMapping("/jsr303")
    @ResponseBody
    public void testJSR(@Validated User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.print(bindingResult.getTarget()+bindingResult.getFieldError().getDefaultMessage());
            return;
        }
    }

    @RequestMapping("/jsr303-2")
    @ResponseBody
    public void testJSR(@Validated({LoginVo.Test2.class}) LoginVo vo){


    }

}
