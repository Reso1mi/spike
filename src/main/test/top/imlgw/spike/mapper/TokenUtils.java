package top.imlgw.spike.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.imlgw.spike.controller.GoodsController;
import top.imlgw.spike.controller.LoginController;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.vo.LoginVo;
import top.imlgw.spike.vo.RegisterVo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author imlgw.top
 * @date 2019/6/4 12:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TokenUtils {

    @Autowired
    private SpikeUserService spikeUserService;

    static String str="0123456789";

    static  Random random = new Random();

    static HttpServletResponse resp;

    @Test
    public  void test() throws IOException {
        FileOutputStream outputStream=new FileOutputStream(new File("D:\\AliyunKey\\config.txt"));
        for (int i=0;i<20000;i++){
            String phone = creatPhone();
            RegisterVo registerVo = new RegisterVo(phone,"123456","user-"+i);
            spikeUserService.register(registerVo);
            //需要将token返回出来
            //String token= spikeUserService.login(resp, new LoginVo(registerVo.getMobile(), registerVo.getPassword()));
            //outputStream.write((phone+","+token+"\n").getBytes());
        }
    }

    public  String creatPhone(){
        String res="1";
        for (int i=0;i<10;i++){
            res+=str.charAt(random.nextInt(10));
        }
        return res;
    }

}
