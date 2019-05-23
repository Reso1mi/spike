package top.imlgw.spike.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imlgw.spike.result.CodeMsg;
import top.imlgw.spike.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller 层异常处理器
 *
 * @author imlgw.top
 * @date 2019/5/13 18:21
 */
@ControllerAdvice
@ResponseBody //直接返回给客户端，需要json的转换
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class) //处理controller层所有异常
    public Result<String> globalExceptionHandle(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            //全局异常
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    //测试Demo 看先处理那个
    @ExceptionHandler(value = BindException.class) //处理BindException
    public Result<String> bindExceptionHandle(HttpServletRequest request, Exception e) {
        BindException ex = (BindException) e;
        List<ObjectError> errors = ex.getAllErrors();
        ObjectError error = errors.get(0);
        String msg = error.getDefaultMessage();
        return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
    }
}
