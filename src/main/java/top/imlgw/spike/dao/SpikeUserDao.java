package top.imlgw.spike.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import top.imlgw.spike.entity.SpikeUser;

/**
 * @author imlgw.top
 * @date 2019/5/12 17:55
 */
@Mapper
@Component //不写这个注解idea有个红线，有点烦
public interface SpikeUserDao {

    @Select("SELECT * FROM spike_user WHERE id=#{id}")
    SpikeUser getById(@Param("id") Long id);

    //INSERT INTO USER(id,name,age,password) values(#{id},#{name},#{age},#{password})
    @Insert("INSERT  INTO spike_user (id,nickname,salt,password,register_date) VALUES (#{id},#{nickname},#{salt},#{password},#{registerDate})")
    Boolean insert(SpikeUser user);
}
