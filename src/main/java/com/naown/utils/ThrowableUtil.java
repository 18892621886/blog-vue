package com.naown.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            // 将异常信息存放到sw类中 比如 / by zero 等等更详细的一些异常信息
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}