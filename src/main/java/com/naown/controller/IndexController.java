package com.naown.controller;

import com.naown.aop.annotation.Log;
import com.naown.quartz.service.ScheduleJobService;
import com.naown.utils.SaltUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @USER: chenjian
 * @DATE: 2021/2/21 1:39 周日
 **/
@RestController
public class IndexController {

    @Autowired
    ScheduleJobService scheduleJobService;

    @GetMapping("test")
    @RequiresAuthentication
    public String test(){
        return "测试";
    }

    @Log("暂停线程")
    @GetMapping("pause")
    public String pause(){
        scheduleJobService.pause(1L);
        return "暂停线程";
    }

    @Log("恢复线程")
    @GetMapping("resume")
    public String resume(){
        scheduleJobService.resume(1L);
        return "恢复线程";
    }



    /**
     * 假设这是功能模块 需要admin角色才能访问
     * @RequiresRoles 表示需要同时具有相同的角色才能访问 logical= Logical.OR添加这个属性则可以表示其中一个角色就可以访问
     * @return
     */
    @Log("功能模块")
    @RequiresRoles(value={"全部", "本级"}, logical= Logical.OR)
    @GetMapping("/role")
    public void role(){
        System.out.println("测试业务");
        // 业务
    }
}
