package com.naown.quartz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.naown.aop.entity.LogEntity;
import com.naown.quartz.entity.QuartzLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author: chenjian
 * @since : 2021/2/23 22:07 周二
 **/
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
    /**
     * 查询所有任务日志，根据时间查询
     * @param page page参数
     * @param queryWrapper MyBatis-plus条件选择器
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    IPage<QuartzLog> listQuartzLog(IPage<QuartzLog> page, @Param(Constants.WRAPPER) Wrapper<LogEntity> queryWrapper, String startDate, String endDate);
}
