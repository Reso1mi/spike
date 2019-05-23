package top.imlgw.spike.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imlgw.spike.dao.SpikeUserDao;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.exception.GlobalException;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.redis.SpikeUserKey;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.utils.MD5Util;
import top.imlgw.spike.utils.UUIDUtil;
import top.imlgw.spike.vo.LoginVo;
import top.imlgw.spike.vo.RegisterVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author imlgw.top
 * @date 2019/5/12 18:03
 */
@Service
public class SpikeUserService {

    public static final String COOK1_NAME_TOKEN="token";

    @Autowired
    private SpikeUserDao spikeUserDao;

    @Autowired
    private RedisService redisService;

    /**
     * Dao相关
     *
     * @param id
     * @return
     */
    public SpikeUser getById(long id) {
        return spikeUserDao.getById(id);
    }

    public Boolean insert(SpikeUser spikeUser) {
        return spikeUserDao.insert(spikeUser);
    }

    /*
    * Session相关
    *
    * */
    public SpikeUser getUserByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        SpikeUser user = redisService.get(SpikeUserKey.tkPrefix, token, SpikeUser.class);
        //重新设置cookie有效期（延长有效期）
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    /**
     * 登陆逻辑
     *
     * @param vo
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVo vo) {
        if (vo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        //看手机号是否存在
        SpikeUser user = getById(Long.parseLong(vo.getMobile()));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //db密码
        String dbPass = user.getPassword();
        //拿到db盐
        String dbSalt = user.getSalt();
        //表单提交过来的密码转换为db加密的格式然后验证是不是相等
        String formPass = MD5Util.formPassToDBPass(vo.getPassword(), dbSalt);
        if (!formPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token =UUIDUtil.getUuid();
        addCookie(response,token,user);
        return true;
    }

    private void addCookie(HttpServletResponse response,String token,SpikeUser user){
        redisService.set(SpikeUserKey.tkPrefix, token, user);
        Cookie cookie = new Cookie(COOK1_NAME_TOKEN, token);
        cookie.setMaxAge(SpikeUserKey.tkPrefix.expireSecond());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 注册逻辑
     *
     * @param vo
     * @return
     */
    public boolean register(RegisterVo vo) {
        //构建这个用户
        SpikeUser user = new SpikeUser();
        //查询这个用户是否存在，可以自定义一个注解来搞
        String mobile = vo.getMobile();
        if (getById(Long.parseLong(mobile)) != null) {
            throw new GlobalException(CodeMsg.ID_REPEAT);
        }

        user.setId(Long.parseLong(mobile));
        user.setNickname(vo.getNickname());
        //生成随机的盐
        String dbSalt = MD5Util.randomSalt(6);
        //将表单加密后的password加盐后再加密
        String dbPass = MD5Util.formPassToDBPass(vo.getPassword(), dbSalt);
        System.out.println(dbPass);
        user.setPassword(dbPass);
        user.setSalt(dbSalt);
        user.setRegisterDate(new Date());
        if (insert(user)) {
            return true;
        }
        throw new GlobalException(CodeMsg.SERVER_ERROR);
    }


}
