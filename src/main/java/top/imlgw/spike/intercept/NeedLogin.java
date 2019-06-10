package top.imlgw.spike.intercept;

import java.lang.annotation.*;

/**
 * @author imlgw.top
 * @date 2019/6/9 15:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedLogin {
    boolean needLogin() default true;
}
