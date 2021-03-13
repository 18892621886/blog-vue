package com.naown.aop.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @author : chenjian
 * @since : 2021/2/11 22:50 周四
 **/
@Service
public class LogServiceImpl implements LogService {

    /** ********************************************** 登录操作日志模块 ********************************************** */

    @Autowired
    private LogMapper logMapper;

    @Override
    public Integer saveLog(String username,String os, String browser,String requestMethod, String ip, ProceedingJoinPoint joinPoint, LogEntity log) {
        // 从切点上获取目标的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 方法上获得注解所在的类和信息
        Log aopLog = method.getAnnotation(Log.class);

        // 方法和路径
        String methodName = joinPoint.getTarget().getClass().getName() + '.' + signature.getName();
        // 如果Log实体不为空则注入注解上标注的信息
        if (aopLog != null){
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
        // 设置操作方法所传入的参数  如果是登录接口则不记录参数 TODO 后续改成常量
        if (!"登录日志".equals(aopLog.value())){
            log.setParams(getParameter(method, joinPoint.getArgs()));
        }
        // 设置浏览器名称
        log.setBrowser(browser);
        // 设置操作系统信息
        log.setOs(os);
        // 设置请求方式 例如POST、GET
        log.setRequestMethod(requestMethod);
        // 保存log到数据库并且返回成功数量
        return logMapper.insert(log);
    }

    /**
     * @param pageNum 第几页
     * @param pageSize 每页几条数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Override
    public IPage<LogEntity> listLogEntity(Integer pageNum, Integer pageSize, String startDate, String endDate,String... params) {
        Page<LogEntity> page = new Page<>(pageNum,pageSize);
        QueryWrapper<LogEntity> queryWrapper = new QueryWrapper<>();
        if (params.length > 0){
            for (String param  : params){
                queryWrapper.like("description",param).or();
            }
        }
        IPage<LogEntity> logEntityIPage = logMapper.listLogEntity(page, queryWrapper,startDate, endDate);
        return logEntityIPage;
    }

    /**
     * @param pageNum 第几页
     * @param pageSize 每页几条数据
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @param params 需要排除的参数
     * @return
     */
    @Override
    public IPage<LogEntity> listOperationLogs(Integer pageNum, Integer pageSize, String startDate, String endDate, String... params) {
        Page<LogEntity> page = new Page<>(pageNum,pageSize);
        return logMapper.listOperationLogs(page,null,startDate,endDate,params);
    }

    /**
     * 按id删除登录日志
     * @param id 日志id
     * @return
     */
    @Override
    public Integer deleteLogById(Long id) {
        return logMapper.deleteById(id);
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
                Map<String, Object> map = new HashMap<>(16);
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
