package top.imlgw.spike.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.imlgw.spike.dao.UserMapper;
import top.imlgw.spike.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserMapperTest{
    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByName(){
        User imlgw = userMapper.findByName("imlgw");
        System.out.println(imlgw);
    }
}