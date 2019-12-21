package top.imlgw.spike.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.imlgw.spike.access.AccessInterceptor;
import top.imlgw.spike.access.AccessLimit;
import top.imlgw.spike.intercept.LoginIntercept;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    SpikeUserArgumentResolver spikeUserArgumentResolver;

    @Autowired
    LoginIntercept loginIntercept;

    @Autowired
    AccessInterceptor accessInterceptor;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(spikeUserArgumentResolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //这里如果是用的模板引擎，就只能是模板引擎template里面的文件
        //这里后面默认指的是static里面的文件，后缀为html
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/goodslist").setViewName("goods_list");
        registry.addViewController("/register").setViewName("register");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //这里不排除login也可以，但是效率可能会低一些
        registry.addInterceptor(loginIntercept);
        registry.addInterceptor(accessInterceptor);
    }

}
