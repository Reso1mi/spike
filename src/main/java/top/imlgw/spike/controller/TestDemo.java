package top.imlgw.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import top.imlgw.spike.dao.UserMapper;


/**
 * @author imlgw.top
 * @date 2019/5/11 14:18
 */
@Controller
public class TestDemo {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/demo")
    public String test(ModelMap model){
        model.addAttribute("name","imlgw.top");
        return  "test";
    }
}
