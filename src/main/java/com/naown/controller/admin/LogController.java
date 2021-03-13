package com.naown.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.naown.aop.entity.LogEntity;
import com.naown.aop.service.LogService;
import com.naown.common.entity.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: chenjian
 * @since: 2021/3/4 21:51 周四
 **/
@RestController
@RequiresAuthentication
@RequestMapping("/admin")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询登录日志列表
     * @param date     按操作时间查询
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return
     */
    @GetMapping("/loginLogs")
    public Result loginLogs(@RequestParam(defaultValue = "", name = "date") String[] date,
                            @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                            @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {
        // 根据时间查询和分页数据实现
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        // 查询登录日志和退出日志
        IPage<LogEntity> logEntities = logService.listLogEntity(pageNum,pageSize,startDate,endDate,"登录日志","退出日志");
        return Result.succeed(logEntities,"请求成功");
    }

    /**
     * 分页查询操作日志列表
     * @param date     按操作时间查询
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return
     */
    @GetMapping("/operationLogs")
    public Result operationLogs(@RequestParam(defaultValue = "") String[] date,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        // 排除登录日志和退出日志
        IPage<LogEntity> logEntities = logService.listOperationLogs(pageNum,pageSize,startDate,endDate,"登录日志","退出日志");
        return Result.succeed(logEntities,"请求成功");
    }

    /**
     * 按id删除登录日志
     * @param id 日志id
     * @return
     */
    @RequestMapping(value = "/deleteLog",method = RequestMethod.DELETE)
    public Result delete(@RequestParam Long id) {
        Integer integer = logService.deleteLogById(id);
        return Result.succeed(integer,"删除成功");
    }
}
