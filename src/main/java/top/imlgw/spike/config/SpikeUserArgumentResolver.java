package top.imlgw.spike.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.utils.UserContext;

/**
 * @author imlgw.top
 * @date 2019/5/15 17:13
 */
@Service
public class SpikeUserArgumentResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        //处理SpikeUser类型的
        return clazz==SpikeUser.class;
    }

    /*
    * 处理SpikeUser参数
    * */
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserContext.getUser();
    }
}
