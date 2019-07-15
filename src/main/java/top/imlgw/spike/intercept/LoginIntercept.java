package top.imlgw.spike.intercept;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.service.SpikeUserService;
import top.imlgw.spike.utils.UserContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author imlgw.top
 * @date 2019/6/8 23:22
 */
@Component
public class LoginIntercept implements HandlerInterceptor {

    @Autowired
    SpikeUserService spikeUserService;

    /**
     * @param request
     * @param response
     * @param handler
     * @return 在登陆前拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod hm=(HandlerMethod) handler;
        SpikeUser spikeUser = getSpikeUser(request, response);
        //有的页面不需要登陆但是需要用户信息，所以需要先存进去
        UserContext.setUser(spikeUser);
        //获取方法上的注解
        NeedLogin needLogin = hm.getMethodAnnotation(NeedLogin.class);
        if(needLogin==null || ! needLogin.needLogin()){
            //没有注解后者注解为false,就直接放过
            return true;
        }
        //有注解，没登陆
        if(spikeUser==null){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }


    /** 视图渲染完毕后调用(收尾工作)
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //删除ThreadLocal中的User否则会产生错乱
        UserContext.removeUser();
    }


    /** 获取 spikeUser
     * @param request
     * @param response
     * @return
     */
    private SpikeUser getSpikeUser(HttpServletRequest request,HttpServletResponse response){
        //拿参数中的token
        String paramToken = request.getParameter(SpikeUserService.COOK_NAME_TOKEN);
        //拿cookie中的token
        String cookieToken = getCookieValue(request, SpikeUserService.COOK_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            //没登陆cookie为空
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        SpikeUser user = spikeUserService.getUserByToken(response, token);
        return user;
    }


    /*
     * 获取cookie中的User
     * */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
