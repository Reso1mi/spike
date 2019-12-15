package top.imlgw.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.dao.UserDao;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.entity.User;
import top.imlgw.spike.rabbitmq.MQSender;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.vo.LoginVo;
/**
 * @author imlgw.top
 * @date 2019/5/11 14:18
 */
@Controller
public class TestDemo {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;
    /*
    * MQ测试
    * */
/*    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("Hello, mq direct! ! !");
        return  Result.success(CodeMsg.SUCCESS);
    }

    @RequestMapping("/mqtopic")
    @ResponseBody
    public Result<String> mqtopic(){
        mqSender.sendTopic("Hello, mq Topic ! ! !");
        return  Result.success(CodeMsg.SUCCESS);
    }

    @RequestMapping("/mqfanout")
    @ResponseBody
    public Result<String> mqfanout(){
        mqSender.sendFanout("Hello, mq fanout ! ! !");
        return  Result.success(CodeMsg.SUCCESS);
    }


    @RequestMapping("/mqheader")
    @ResponseBody
    public Result<String> mqheader(){
        mqSender.sendHeader("Hello, mq header ! ! !");
        return  Result.success(CodeMsg.SUCCESS);
    }*/

    /*
    *框架测试
    * */
    @RequestMapping("/demo")
    public String test(ModelMap model){
        model.addAttribute("name","imlgw.top");
        System.out.println("dsdsad");
        return  "test";
    }

/*    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("Hello world");
    }*/

    /*
    *Redis 压测
    * 5000*10 QPS 3000左右
    * */
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet(SpikeUser spikeUser){
        //System.out.println(spikeUser);
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
