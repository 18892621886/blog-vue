package com.naown.shiro.exception;

/**
 * 自定义异常类
 * @author chenjian
 */
public class CustomException extends RuntimeException {

    public CustomException(String msg){
        super(msg);
    }

    public CustomException() {
        super();
    }
}