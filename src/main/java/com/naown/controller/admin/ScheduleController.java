package com.naown.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naown.aop.annotation.Log;
import com.naown.common.entity.Result;
import com.naown.quartz.ScheduleJob;
import com.naown.quartz.entity.QuartzJob;
import com.naown.quartz.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/11 21:13 周四
 **/
@RestController
@RequestMapping("/admin")
public class ScheduleController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 分页查询定时任务列表
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/jobs")
    public Result jobs(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<QuartzJob> jobList = scheduleJobService.getJobList(pageNum, pageSize);
        return Result.succeed(jobList,"请求成功");
    }

    /**
     * 更新任务状态：暂停或恢复
     * @param jobId  任务id
     * @param status 状态
     * @return
     */
    @Log("更新任务状态")
    @PutMapping("/job/status")
    public Result updateJobStatus(@RequestParam Long jobId, @RequestParam Boolean status) {
        scheduleJobService.updateJobStatusById(jobId,status);
        return Result.succeed("更新成功");
    }

    /**
     * 立即执行任务(只执行一次)
     * @param jobId 任务id
     * @return
     */
    @Log("立即执行定时任务")
    @PostMapping("/job/run")
    public Result runJob(@RequestParam Long jobId) {
        scheduleJobService.runJobById(jobId);
        return Result.succeed("提交执行");
    }

    /**
     * 删除定时任务
     * @param jobId 任务id
     * @return
     */
    @Log("删除定时任务")
    @RequiresRoles("超级管理员")
    @DeleteMapping("/job/delete")
    public Result deleteJob(@RequestParam Long jobId) {
        scheduleJobService.deleteJobById(jobId);
        return Result.succeed("删除成功");
    }

    /**
     * TODO 在此备注一下，此时返回结果的msg暂时都没有用到，后续想改了可以讲此消息赋值到提示框中，以后如果想改信息直接后台就可以修改了
     * 修改定时任务
     * @param quartzJob
     * @return
     */
    @Log("修改定时任务")
    @PutMapping("/job/edit")
    public Result updateJob(@RequestBody QuartzJob quartzJob) {
        //scheduleJob.setStatus(false);
        //ValidatorUtils.validateEntity(scheduleJob);
        scheduleJobService.updateJob(quartzJob);
        return Result.succeed("修改成功");
    }
}
