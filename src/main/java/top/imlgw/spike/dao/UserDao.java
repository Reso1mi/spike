package top.imlgw.spike.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import top.imlgw.spike.entity.User;

/**
 * @author imlgw.top
 * @date 2019/5/11 15:59
 */
@Mapper
@Component
public interface UserDao {

    @Select("SELECT * FROM USER WHERE NAME = #{name}")
    User findByName(@Param("name") String name);

    @Insert("INSERT INTO USER(id,name,age,password) values(#{id},#{name},#{age},#{password})")
    boolean insert(User user);
}
