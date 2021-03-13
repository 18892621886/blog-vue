package com.naown.aop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naown.aop.entity.LogEntity;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Log日志对应的Service
 * @author : chenjian
 * @since : 2021/2/11 22:50 周四
 **/
public interface LogService {

    /** ********************************************** 登录操作日志模块 ********************************************** */

    /**
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param requestMethod 请求方式 例如POST、GET
     * @param ip 用户IP
     * @param joinPoint /
     * @param log 日志实体
     * @param os 操作系统
     * @return 存储成功条数
     */
    Integer saveLog(String username,String os, String browser,String requestMethod, String ip, ProceedingJoinPoint joinPoint, LogEntity log);

    /**
     * 查询全部登录log日志
     * @param pageNum 第几页
     * @param pageSize 每页几条数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param params 需要排除的参数
     * @return 返回page封装对象
     */
    IPage<LogEntity> listLogEntity(Integer pageNum, Integer pageSize, String startDate, String endDate, String... params);

    /**
     * 查询登录退出以外的操作日志
     * @param pageNum 第几页
     * @param pageSize 每页几条数据
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @param params 需要排除的参数
     * @return 返回page封装对象
     */
    IPage<LogEntity> listOperationLogs(Integer pageNum, Integer pageSize, String startDate, String endDate, String... params);

    /**
     * 按id删除登录日志
     * @param id 日志id
     * @return
     */
    Integer deleteLogById(Long id);

}
