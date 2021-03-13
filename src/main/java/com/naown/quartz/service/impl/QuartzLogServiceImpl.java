package com.naown.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naown.aop.entity.LogEntity;
import com.naown.quartz.entity.QuartzLog;
import com.naown.quartz.mapper.QuartzLogMapper;
import com.naown.quartz.mapper.QuartzMapper;
import com.naown.quartz.service.QuartzLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/6 23:14 周六
 **/
@Service
public class QuartzLogServiceImpl implements QuartzLogService {

    @Autowired
    private QuartzLogMapper quartzLogMapper;

    /**
     * 获取全部任务实例日志
     * @param pageNum 第几页
     * @param pageSize 一页几条数据
     * @return 所有任务执行记录
     */
    @Override
    public IPage<QuartzLog> listQuartzLogs(Integer pageNum, Integer pageSize) {
        Page<QuartzLog> page = new Page<>(pageNum,pageSize);
        return quartzLogMapper.selectPage(page,null);
    }

    /**
     * 根据id删除日志
     * @param id 任务id
     * @return 删除条数
     */
    @Override
    public Integer deleteQuartzLogById(Long id) {
        return quartzLogMapper.deleteById(id);
    }

    /**
     * 查询所有任务日志，根据时间查询
     * @param pageNum 第几页
     * @param pageSize 一页几条数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Override
    public IPage<QuartzLog> listQuartzLog(Integer pageNum, Integer pageSize, String startDate, String endDate) {
        Page<QuartzLog> page = new Page<>(pageNum,pageSize);
        return quartzLogMapper.listQuartzLog(page,null,startDate,endDate);
    }
}
