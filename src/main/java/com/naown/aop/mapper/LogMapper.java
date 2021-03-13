package com.naown.aop.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.naown.aop.entity.LogEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Mybtis-Plus Mapper
 * @author : chenjian
 * @since : 2021/2/11 21:46 周四
 **/
public interface LogMapper extends BaseMapper<LogEntity> {

    /**
     * 自定义sql 根据时间查询数据
     * @param page 分页参数
     * @param startDate 查询开始时间
     * @param queryWrapper MyBatis-plus条件构造器
     * @param endDate 查询结束时间
     * @param params 需要排除的参数
     * @return 返回page封装对象
     */
    IPage<LogEntity> listLogEntity(IPage<LogEntity> page,@Param(Constants.WRAPPER)Wrapper<LogEntity> queryWrapper, String startDate, String endDate, String... params);

    /**
     * 查询登录退出以外的操作日志
     * @param page 分页参数
     * @param queryWrapper MyBatis-plus条件构造器
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @param params 需要排除的参数
     * @return 返回page封装对象
     */
    IPage<LogEntity> listOperationLogs(IPage<LogEntity> page, @Param(Constants.WRAPPER) Wrapper<LogEntity> queryWrapper, String startDate, String endDate, String... params);
}
