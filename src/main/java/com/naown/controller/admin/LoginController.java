package com.naown.controller.admin;

import com.naown.aop.annotation.Log;
import com.naown.common.entity.LoginInfo;
import com.naown.common.entity.Result;
import com.naown.common.service.MenusService;
import com.naown.shiro.entity.User;
import com.naown.shiro.service.UserService;
import com.naown.utils.JwtUtils;
import com.naown.utils.ShiroUtils;
import com.naown.utils.common.Constant;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: chenjian
 * @since: 2021/3/1 23:04 周一
 **/
@RestController
@RequestMapping("/admin")
public class LoginController {


    @Autowired
    private MenusService menusService;

    @Autowired
    private UserService userService;

    /**
     * 2020.3.2
     * 首页登录逻辑编写
     * @param loginInfo
     * @return
     */
    @Log("登录日志")
    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo, HttpServletResponse response) {

        User user = userService.findByUserNameRole(loginInfo.getUsername());
        if (user == null) {
            throw new RuntimeException("该帐号不存在(The account does not exist.)");
        }
        // 密码解密，暂时没做

        if (user.getPassword().equals(loginInfo.getPassword())) {
            // 清除可能存在的Shiro权限信息缓存
            /*if (JedisUtil.exists(Constant.PREFIX_SHIRO_CACHE + userDto.getAccount())) {
                JedisUtil.delKey(Constant.PREFIX_SHIRO_CACHE + userDto.getAccount());
            }*/
            // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            // JedisUtil.setObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userDto.getAccount(), currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));
            // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
            String token = JwtUtils.sign(user.getUsername(), currentTimeMillis);
            Map<String, Object> map = new HashMap<>(16);
            map.put("token", token);
            map.put("menu",menusService.listMenusByRoleId(user.getId()));
            // TODO 返回前端的数据应该再次封装成DTO 避免敏感数据传出
            map.put("user",user);
            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            return Result.succeed(map,"登录成功(Login Success.)");
        } else {
            throw new RuntimeException("帐号或密码错误(Account or Password Error.)");
        }

        // 使用shiro进行登录验证，判断逻辑在自定义realm里面实现，如果账号和密码错误会抛出异常，已全局异常处理
        // ShiroUtils.getSubject().login(new UsernamePasswordToken(loginInfo.getUsername(),loginInfo.getPassword()));
        // TODO 此处应该动态修改menus的值 2021.3.3完成此次修改 并且完成首页大部分数据渲染
        /*Map<String, Object> map = new HashMap<>(16);
        User user = (User) ShiroUtils.getSubject().getPrincipal();
        if (!Objects.isNull(user)){
            map.put("token", UUID.randomUUID());
            map.put("menu",menusService.listMenusByRoleId(user.getId()));
            TODO 返回前端的数据应该再次封装成DTO 避免敏感数据传出
            map.put("user",user);
            return Result.succeed(map);
        }else {
            return Result.error("系统异常,请稍后再试");
        }*/
    }
}
