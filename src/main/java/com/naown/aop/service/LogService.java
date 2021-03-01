package com.naown.aop.service;

import com.naown.aop.entity.LogEntity;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Log日志对应的Service
 * @USER: chenjian
 * @DATE: 2021/2/11 22:50 周四
 **/
public interface LogService {

    /**
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param ip 用户IP
     * @param joinPoint /
     * @param log 日志实体
     * @return 存储成功条数
     */
    Integer saveLog(String username, String browser, String ip, ProceedingJoinPoint joinPoint, LogEntity log);
}
