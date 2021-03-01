package com.naown.aop.service.impl;

import com.alibaba.fastjson.JSON;
import com.naown.aop.annotation.Log;
import com.naown.aop.entity.LogEntity;
import com.naown.aop.mapper.LogMapper;
import com.naown.aop.service.LogService;
import com.naown.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Log日志实现类
 * @USER: chenjian
 * @DATE: 2021/2/11 22:50 周四
 **/
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public Integer saveLog(String username, String browser, String ip, ProceedingJoinPoint joinPoint, LogEntity log) {
        // 从切点上获取目标的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 方法上获得注解所在的类和信息
        Log aopLog = method.getAnnotation(Log.class);

        // 方法和路径
        String methodName = joinPoint.getTarget().getClass().getName() + '.' + signature.getName();
        // 如果Log实体不为空则注入注解上标注的信息
        if (log != null){
            log.setDescription(aopLog.value());
        }

        // 设置访问者的IP
        log.setRequestIp(ip);
        // 设置访问者的地址
        log.setAddress(StringUtils.getLocalCityInfo(log.getRequestIp()));
        // 设置访问者所用的全路径类名和方法
        log.setMethod(methodName);
        // 设置操作的访问者昵称
        log.setUsername(username);
        // 设置操作方法所传入的参数
        log.setParams(getParameter(method, joinPoint.getArgs()));
        // 设置浏览器名称
        log.setBrowser(browser);
        // 保存log到数据库并且返回成功数量
        return logMapper.insert(log);
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return "";
        }
        return argList.size() == 1 ? JSON.toJSONString(argList.get(0)) : JSON.toJSONString(argList);
    }


}
