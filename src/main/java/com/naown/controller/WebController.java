package com.naown.controller;

import com.naown.common.entity.Result;
import com.naown.common.service.BacklogService;
import com.naown.shiro.entity.User;
import com.naown.shiro.service.UserService;
import com.naown.utils.EmailUtils;
import com.naown.utils.JwtUtils;
import com.naown.utils.RedisUtils;
import com.naown.utils.SpringContextUtils;
import com.naown.utils.common.Constant;
import com.naown.utils.entity.EmailConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: chenjian
 * @since: 2021/3/12 21:54 周五
 **/
@RestController
public class WebController {

    @Autowired
    private UserService userService;

    @PostMapping("/testLogin")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password, HttpServletResponse httpServletResponse) {
        User user = userService.findByUserNameRole(username);
        if (user.getPassword().equals(password)) {
            String token = JwtUtils.sign(username, String.valueOf(System.currentTimeMillis()));
            httpServletResponse.setHeader("Authorization", token);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return Result.succeed(JwtUtils.sign(username, password),"Login success");
        } else {
            throw new UnauthorizedException();
        }
    }
    @GetMapping("/article")
    public Result article() {
        Subject subject = SecurityUtils.getSubject();
        boolean user = RedisUtils.hasKey("shiro:hash");
        System.out.println(RedisUtils.hget(Constant.PREFIX_SHIRO_HASH_CACHE,Constant.PREFIX_SHIRO_CACHE+"admin").toString());
        if (subject.isAuthenticated()) {
            return Result.succeed("You are already logged in");
        } else {
            return Result.succeed("You are guest");
        }
    }

    @GetMapping("/require_auth")
    @RequiresAuthentication
    public Result requireAuth() {
        return Result.succeed("You are authenticated");
    }

    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public Result requireRole() {
        return Result.succeed("You are visiting require_role");
    }

    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Result requirePermission() {
        return Result.succeed("You are visiting permission require edit,view");
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result unauthorized() {
        return Result.succeed(401, "Unauthorized");
    }

    @RequestMapping("email")
    public Result email() {
        try {
            EmailUtils.sendEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.succeed("发送邮件");
    }

    @RequestMapping("redis")
    public Result getRedis(){
        Long aLong = RedisUtils.getExpire("shiro:cache:admin");
        return Result.succeed(aLong);
    }
}
