package com.naown.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @USER: chenjian
 * @DATE: 2021/2/11 21:33 周四
 **/
public class RequestHolder {

    public static HttpServletRequest getHttpServletRequest(){
        // 获得HttpServletRequest并返回
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
