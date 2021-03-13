package com.naown.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naown.quartz.entity.QuartzJob;
import com.naown.quartz.mapper.QuartzMapper;
import com.naown.quartz.service.ScheduleJobService;
import com.naown.quartz.utils.ScheduleUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @description: 定时任务业务层实现
 * @author : Naccl
 * @since : 2020-11-01
 * // TODO 需要完善service
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
    @Autowired
    Scheduler scheduler;
    @Autowired
    private QuartzMapper quartzMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<QuartzJob> quartzJobList = getJobList();
        for (QuartzJob quartzJob : quartzJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, quartzJob.getId());
            //如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, quartzJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, quartzJob);
            }
        }
    }

    /**
     * 获取所有的Job任务
     * @return 返回所有JOB
     */
    @Override
    public List<QuartzJob> getJobList() {
        return quartzMapper.selectList(null);
    }

    /**
     * 分页查询所有的job任务
     * @param pageNum 第几页
     * @param pageSize 一页几条数据
     * @return
     */
    @Override
    public IPage<QuartzJob> getJobList(Integer pageNum, Integer pageSize) {
        IPage<QuartzJob> page = new Page<>(pageNum,pageSize);
        return quartzMapper.selectPage(page,null);
    }

    /**
     * 保存一个Job实例
     * @param quartzJob JOB任务实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJob(QuartzJob quartzJob) {
        if (quartzMapper.insert(quartzJob) != 1) {
            throw new PersistenceException("添加失败");
        }
        ScheduleUtils.createScheduleJob(scheduler, quartzJob);
    }

    /**
     * 更新一个Job实例
     * @param quartzJob 更新的JOB实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(QuartzJob quartzJob) {
        if (quartzMapper.updateById(quartzJob) != 1) {
            throw new PersistenceException("更新失败");
        }
        ScheduleUtils.updateScheduleJob(scheduler, quartzJob);
    }

    /**
     * 删除一个Job实例
     * @param jobId 任务ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobById(Long jobId) {
        ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        if (quartzMapper.deleteById(jobId) != 1) {
            throw new PersistenceException("删除失败");
        }
    }

    /**
     * 立即执行一个Job实例（暂时发现立即运行只会执行一次）
     * @param jobId 任务ID
     */
    @Override
    public void runJobById(Long jobId) {
        ScheduleUtils.run(scheduler, quartzMapper.selectById(jobId));
    }

    /**
     * 更新Job的运行状态
     * @param jobId 更新的JobId
     * @param status 状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobStatusById(Long jobId, Boolean status) {
        QuartzJob quartzJob = quartzMapper.selectById(jobId);
        if (quartzJob.getStatus().equals(status)){
            throw new PersistenceException("修改失败,Job状态冲突，不能给已经启动或停止的任务再修改同样的状态");
        }
        if (status) {
            ScheduleUtils.resumeJob(scheduler, quartzJob.getId());
            quartzJob.setStatus(true);
        } else {
            ScheduleUtils.pauseJob(scheduler, quartzJob.getId());
            quartzJob.setStatus(false);
        }
        if (quartzMapper.updateById(quartzJob) != 1) {
            throw new PersistenceException("修改失败");
        }
    }

    /**
     * 根据时间获取Job实例
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Override
    public List<QuartzJob> getJobLogListByDate(String startDate, String endDate) {
        // 暂未用到未实现
       /* return scheduleJobLogMapper.getJobLogListByDate(startDate, endDate);*/
        return null;
    }

    /**
     * 暂停运行
     * @param jobId 任务ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long jobId) {
        ScheduleUtils.pauseJob(scheduler, jobId);
        QuartzJob quartzJob = quartzMapper.selectById(jobId);
        quartzJob.setStatus(!quartzJob.getStatus());
        quartzMapper.updateById(quartzJob);
    }

    /**
     * 恢复运行
     * @param jobId 任务ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long jobId) {
        ScheduleUtils.resumeJob(scheduler, jobId);
        QuartzJob quartzJob = quartzMapper.selectById(jobId);
        quartzJob.setStatus(!quartzJob.getStatus());
        quartzMapper.updateById(quartzJob);
    }

    /**
     * 根据Id获取一个Job实例
     * @param jobId 任务ID
     * @return
     */
    @Override
    public QuartzJob selectJobById(Long jobId) {
        return quartzMapper.selectById(jobId);
    }

}
