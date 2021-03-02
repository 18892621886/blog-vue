package com.naown.controller.admin;

import com.naown.common.entity.LoginInfo;
import com.naown.common.entity.Menus;
import com.naown.common.entity.Result;
import com.naown.common.service.MenusService;
import com.naown.shiro.entity.User;
import com.naown.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: chenjian
 * @since: 2021/3/1 23:04 周一
 **/
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenusService menusService;

    // TODO 2021 3.2完成动态菜单返回
    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo) {
        User userNameRole = userService.findByUserNameRole(loginInfo.getUsername());
        System.out.println(userNameRole);
        //User user = userService.findUserByUsernameAndPassword(loginInfo.getUsername(), loginInfo.getPassword());
        /*if (!"ROLE_admin".equals(user.getRole())) {
            return Result.create(403, "无权限");
        }*/
        /*user.setPassword(null);
        String jwt = JwtUtils.generateToken("admin:" + user.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", jwt);*/
        Map<String, Object> map = new HashMap<>(16);
        map.put("token", UUID.randomUUID());
        map.put("menu",menusService.listMenus());
        return Result.succeed(map);
    }
}
