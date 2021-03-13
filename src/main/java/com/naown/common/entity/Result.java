package com.naown.common.entity;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @author : chenjian
 * @since : 2021/2/21 17:05 周日
 **/
public class Result extends HashMap<String,Object> {
    public Result(){
        put("code", HttpStatus.OK.value());
        put("msg", "success");
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static Result succeed(Object data) {
        Result result = new Result();
        result.put("data",data);
        return result;
    }
    /**
     * 成功
     * @param msg
     * @return
     */
    public static Result succeed(String msg) {
        Result result = new Result();
        result.put("meg",msg);
        return result;
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static Result succeed(Object data,String msg) {
        Result result = new Result();
        result.put("data",data);
        result.put("msg",msg);
        return result;
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    /**
     * 自定义异常
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return error(500, msg);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
