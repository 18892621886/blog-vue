package com.naown.controller;

import com.naown.aop.annotation.Log;
import com.naown.shiro.dto.LoginDTO;
import com.naown.utils.SaltUtils;
import com.naown.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @USER: chenjian
 * @DATE: 2021/2/12 2:01 周五
 **/

@RestController
public class TestController {

    @Log("测试日志")
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    /**
     * 假设这是注册
     * @return
     */
    @Log("注册日志")
    @GetMapping("/register")
    public String register(){
        String salt = SaltUtils.getSalt(8);
        System.out.println(salt);
        /** 根据密码生成MD5加随机盐的密钥，散列16次 盐必须要保存到数据库,后面解析需要用到 */
        Md5Hash md5Hash = new Md5Hash("123456",salt,16);
        return md5Hash.toHex();
    }

    /**
     * 假设这是登录
     * @return
     */
    @Log("登录日志")
    @PostMapping("/login")
    public String login(@Validated @RequestBody LoginDTO loginDTO){
        ShiroUtils.getSubject().login(new UsernamePasswordToken(loginDTO.getUsername(),loginDTO.getPassword()));
        return "200";
    }

    /**
     * 假设这是退出
     * @return
     */
    @Log("退出日志")
    @PostMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "200";
    }

}
