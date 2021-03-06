package com.naown.aop.aspect;

import com.naown.aop.entity.LogEntity;
import com.naown.aop.service.impl.LogServiceImpl;
import com.naown.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : chenjian
 * @since : 2021/2/11 17:17 周四
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {

    private LogServiceImpl logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(@Autowired LogServiceImpl logService){
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.naown.aop.annotation.Log)")
    public void logPointcut(){}

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 设置执行开始的时间
        currentTime.set(System.currentTimeMillis());
        // 先执行方法，因为不执行的话用户名拿不到
        Object proceed = joinPoint.proceed();
        // 创建log并且制定级别和执行时间
        LogEntity log = new LogEntity("INFO",System.currentTimeMillis() - currentTime.get());
        // 移除刚开始存储的时间
        currentTime.remove();
        // 获得HttpServletRequest
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        // 使用mybatis-plus进行存储
        logService.saveLog(getUsername(),StringUtils.getSystem(request), StringUtils.getBrowser(request),request.getMethod(),StringUtils.getIp(request),joinPoint,log);
        return proceed;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        // 设置执行开始的时间
        currentTime.set(System.currentTimeMillis());
        // 创建log并且制定级别和执行时间
        LogEntity log = new LogEntity("ERROR",System.currentTimeMillis() - currentTime.get());
        // 移除刚开始存储的时间
        currentTime.remove();
        // 设置Log实体类的异常详情
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        // 获得HttpServletRequest
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        // 使用mybatis-plus进行存储
        logService.saveLog(getUsername(),StringUtils.getSystem(request), StringUtils.getBrowser(request),request.getMethod(), StringUtils.getIp(request), (ProceedingJoinPoint)joinPoint, log);
    }

    /**
     * 从token中解析用户名
     * claim 是在生成jwt时的Key 当前存储的是username
     * @return
     */
    public String getUsername() {
        try {
            String token = (String)SecurityUtils.getSubject().getPrincipal();
            return JwtUtils.getClaim(token,"username");
        }catch (Exception e){
            return "";
        }
    }
}
