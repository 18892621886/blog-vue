package com.naown;

import com.naown.aop.mapper.LogMapper;
import com.naown.common.entity.Menus;
import com.naown.common.mapper.MenusMapper;
import com.naown.shiro.entity.User;
import com.naown.shiro.mapper.UserMapper;
import com.naown.utils.EmailUtils;
import com.naown.utils.JwtUtils;
import com.naown.utils.SpringContextUtils;
import com.naown.utils.entity.EmailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.List;

@SpringBootTest
class NaOwnApplicationTests {

    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    @Autowired
    LogMapper logMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MenusMapper menusMapper;
    @Test
    void contextLoads() {
        //User admin = userMapper.findByUserNameRole("admin");
        //System.out.println(admin);
        //System.out.println(menusMapper.listMenus().toString());
        List<Menus> menus = menusMapper.listMenusByRoleId(1L);
        System.out.println(menus);
        /*RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        System.out.println(redisTemplate);*/
        //int info = logMapper.insert(new LogEntity("INFO", System.currentTimeMillis() - (System.currentTimeMillis() + 60)));
        //System.out.println(info);
    }

    public static void main(String[] args) {
        String token = JwtUtils.sign("admin", String.valueOf(System.currentTimeMillis()));
        System.out.println(token);
        String admin = JwtUtils.getClaim(token, "username");
        System.out.println(admin);
        System.out.println(JwtUtils.verify(token));
    }

    @Test
    public void email() throws Exception {

    }

}
