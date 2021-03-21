package com.naown.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naown.aop.annotation.Log;
import com.naown.aop.entity.LogEntity;
import com.naown.common.entity.Result;
import com.naown.quartz.ScheduleJob;
import com.naown.quartz.entity.QuartzLog;
import com.naown.quartz.service.QuartzLogService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/6 23:21 周六
 **/
@RestController
@RequestMapping("/admin")
public class QuartzLogController {

    @Autowired
    private QuartzLogService quartzLogService;

    /**
     * 分页查询操作日志列表
     * @param date     按操作时间查询
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return
     */
    @GetMapping("/quartzLogs")
    public Result operationLogs(@RequestParam(defaultValue = "") String[] date,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        IPage<QuartzLog> quartzLogs = quartzLogService.listQuartzLog(pageNum,pageSize,startDate,endDate);
        return Result.succeed(quartzLogs,"请求成功");
    }

    /**
     * 按id删除任务日志
     * @param id 日志id
     * @return
     */
    @RequiresRoles("超级管理员")
    @RequestMapping(value = "/deleteQuartzLog",method = RequestMethod.DELETE)
    public Result delete(@RequestParam Long id) {
        Integer integer = quartzLogService.deleteQuartzLogById(id);
        return Result.succeed(integer,"删除成功");
    }
}
