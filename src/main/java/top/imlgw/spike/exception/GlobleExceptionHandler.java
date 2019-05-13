package top.imlgw.spike.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;

/**
 * @author imlgw.top
 * @date 2019/5/13 18:21
 */
@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

/*    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandle(HttpServletRequest request,Exception e){
        if(e instanceof BindException){
            BindException ex=(BindException) e;
        }
    }*/
}
