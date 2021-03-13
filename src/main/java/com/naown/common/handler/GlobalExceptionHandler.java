package com.naown.common.handler;

import com.naown.common.entity.Result;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author : chenjian
 * @since : 2021/2/21 17:17 周日
 **/
//@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    //@ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        log.error("系统内部异常，异常信息为:{}", e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"系统内部异常");
    }

    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    //@ExceptionHandler(value = RuntimeException.class)
    public Result runtimeHandle(RuntimeException exception){
        log.error("运行时异常:{}",exception.getMessage());
        return Result.error(exception.getMessage());
    }

    /**
     * TODO 还需要继续优化
     * @param
     * @return
     */
    @ExceptionHandler(value = ShiroException.class)
    public Result handleException(ShiroException e) {
        if (e instanceof UnknownAccountException){
            return Result.error(HttpStatus.UNAUTHORIZED.value(),"用户名或密码错误！");
        }else if (e instanceof IncorrectCredentialsException){
            return Result.error(HttpStatus.UNAUTHORIZED.value(),"用户名或密码错误！");
        }else{
            System.out.println(e.getClass().toString());
            log.error("shiro异常:{}", e.getMessage());
            return Result.error(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        }
    }
}
