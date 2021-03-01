package com.naown.quartz;

import com.naown.quartz.entity.QuartzJob;
import com.naown.quartz.entity.QuartzLog;
import com.naown.quartz.mapper.QuartzLogMapper;
import com.naown.quartz.service.ScheduleJobService;
import com.naown.utils.SpringContextUtils;
import com.naown.utils.ThreadPoolExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 执行中需要记录日志或者添加功能可以在此修改
 * @USER: chenjian
 * @DATE: 2021/2/14 0:48 周日
 **/
@Slf4j
public class ScheduleJob extends QuartzJobBean {
    private ExecutorService service = ThreadPoolExecutorUtil.getPoll();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_PARAM_KEY);
        //获取spring bean
        QuartzLogMapper quartzLogMapper = SpringContextUtils.getBean(QuartzLogMapper.class);
        //数据库保存任务执行记录
        QuartzLog quartzLog = new QuartzLog();
        quartzLog.setId(quartzJob.getId());
        quartzLog.setBeanName(quartzJob.getBeanName());
        quartzLog.setMethodName(quartzJob.getMethodName());
        quartzLog.setParams(quartzJob.getParams());
        quartzLog.setCronExpression(quartzJob.getCronExpression());
        quartzLog.setJobName(quartzJob.getJobName());
        //任务开始时间
        long startTime = System.currentTimeMillis();
        //执行任务
        log.info("任务准备执行，任务ID：{}", quartzJob.getId());
        try {
            ScheduleRunnable task = new ScheduleRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = service.submit(task);
            future.get();
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            //任务执行结果
            quartzLog.setSuccessStatus(true);
            log.info("任务执行成功，任务ID：{}，总共耗时：{} 毫秒", quartzJob.getId(), times);
        } catch (Exception e) {
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            //任务执行结果
            quartzLog.setSuccessStatus(false);
            quartzLog.setExceptionDetail(e.toString());
            log.error("任务执行失败，任务ID：{}", quartzJob.getId(), e);
        } finally {
            quartzLogMapper.insert(quartzLog);
        }
    }
}