package com.naown.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naown.quartz.entity.QuartzLog;

/**
 * 定时任务日志类service
 * @author: chenjian
 * @since: 2021/3/6 23:12 周六
 **/
public interface QuartzLogService {
    /**
     * 获取全部任务实例日志
     * @param pageNum 第几页
     * @param pageSize 一页几条数据
     * @return 所有任务执行记录
     */
    IPage<QuartzLog> listQuartzLogs(Integer pageNum, Integer pageSize);

    /**
     * 根据id删除日志
     * @param id 任务id
     * @return 删除条数
     */
    Integer deleteQuartzLogById(Long id);

    /**
     * 查询所有任务日志，根据时间查询
     * @param pageNum 第几页
     * @param pageSize 一页几条数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    IPage<QuartzLog> listQuartzLog(Integer pageNum, Integer pageSize, String startDate, String endDate);
}
