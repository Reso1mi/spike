package top.imlgw.spike.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.exception.GlobalException;
import top.imlgw.spike.redis.AccessKey;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;
import top.imlgw.spike.utils.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author imlgw.top
 * @date 2019/12/21 17:12
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod hm= (HandlerMethod) handler;
            AccessLimit accessLimit= hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit ==null){
                return  true;
            }
            SpikeUser spikeUser= UserContext.getUser();
            int second = accessLimit.second();
            int maxCount= accessLimit.maxCount();
            String requestURI = request.getRequestURI();
            String key=requestURI+"_"+spikeUser.getId();
            Integer accessCount= redisService.get(AccessKey.withExpire(second), key, Integer.class);
            if (accessCount==null){
                redisService.set(AccessKey.withExpire(second),key,1); //初始值
            }else if (accessCount<maxCount){
                redisService.incr(AccessKey.withExpire(second),key);
            }else {
                throw new GlobalException(CodeMsg.ACCESS_LIMIT);
            }
        }
        return true;
    }
}
