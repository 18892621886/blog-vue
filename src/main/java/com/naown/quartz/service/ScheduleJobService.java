package com.naown.quartz.service;

import com.naown.quartz.ScheduleJob;
import com.naown.quartz.entity.QuartzJob;

import java.util.List;

/**
 * @USER: chenjian
 * @DATE: 2021/2/14 0:52 周日
 **/
public interface ScheduleJobService {
    /**
     * 获取所有的Job任务
     * @return
     */
    List<QuartzJob> getJobList();

    /**
     * 保存一个Job实例
     * @param quartzJob
     */
    void saveJob(QuartzJob quartzJob);

    /**
     * 更新一个Job实例
     * @param quartzJob
     */
    void updateJob(QuartzJob quartzJob);

    /**
     * 删除一个Job实例
     * @param jobId
     */
    void deleteJobById(Long jobId);

    /**
     * 立即执行一个Job实例（暂时发现立即运行只会执行一次）
     * @param jobId
     */
    void runJobById(Long jobId);

    /**
     * 更新Job的运行状态
     * @param quartzJob
     * @param status
     */
    void updateJobStatusById(QuartzJob quartzJob, Boolean status);

    /**
     * 根据时间获取Job实例
     * @param startDate
     * @param endDate
     * @return
     */
    List<QuartzJob> getJobLogListByDate(String startDate, String endDate);

    /**
     * 暂停运行
     */
    void pause(Long jobId);

    /**
     * 恢复运行
     */
    void resume(Long jobId);

    /**
     * 根据Id获取一个Job实例
     */
    QuartzJob selectJobById(Long jobId);
}
