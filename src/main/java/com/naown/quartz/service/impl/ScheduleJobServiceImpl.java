package com.naown.quartz.service.impl;

/**
 * @USER: chenjian
 * @DATE: 2021/2/14 0:54 周日
 **/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @Description: 定时任务业务层实现
 * @Author: Naccl
 * @Date: 2020-11-01
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

    @Override
    public List<QuartzJob> getJobList() {
        return quartzMapper.selectList(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJob(QuartzJob quartzJob) {
        if (quartzMapper.insert(quartzJob) != 1) {
            throw new PersistenceException("添加失败");
        }
        ScheduleUtils.createScheduleJob(scheduler, quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(QuartzJob quartzJob) {
        if (quartzMapper.updateById(quartzJob) != 1) {
            throw new PersistenceException("更新失败");
        }
        ScheduleUtils.updateScheduleJob(scheduler, quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobById(Long jobId) {
        ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        if (quartzMapper.deleteById(jobId) != 1) {
            throw new PersistenceException("删除失败");
        }
    }

    @Override
    public void runJobById(Long jobId) {
        ScheduleUtils.run(scheduler, quartzMapper.selectById(jobId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobStatusById(QuartzJob quartzJob, Boolean status) {
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

    @Override
    public List<QuartzJob> getJobLogListByDate(String startDate, String endDate) {
        // 暂未用到未实现
       /* return scheduleJobLogMapper.getJobLogListByDate(startDate, endDate);*/
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long jobId) {
        ScheduleUtils.pauseJob(scheduler, jobId);
        QuartzJob quartzJob = quartzMapper.selectById(jobId);
        quartzJob.setStatus(!quartzJob.getStatus());
        quartzMapper.updateById(quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long jobId) {
        ScheduleUtils.resumeJob(scheduler, jobId);
        QuartzJob quartzJob = quartzMapper.selectById(jobId);
        quartzJob.setStatus(!quartzJob.getStatus());
        quartzMapper.updateById(quartzJob);
    }

    @Override
    public QuartzJob selectJobById(Long jobId) {
        return quartzMapper.selectById(jobId);
    }

}
