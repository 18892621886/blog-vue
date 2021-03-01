package com.naown;

import com.naown.aop.mapper.LogMapper;
import com.naown.shiro.entity.User;
import com.naown.shiro.mapper.UserMapper;
import com.naown.utils.SpringContextUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;

@SpringBootTest
class NaOwnApplicationTests {

    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    @Autowired
    LogMapper logMapper;

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        //User admin = userMapper.findByUserNameRole("admin");
        //System.out.println(admin);

        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        System.out.println(redisTemplate);
        //int info = logMapper.insert(new LogEntity("INFO", System.currentTimeMillis() - (System.currentTimeMillis() + 60)));
        //System.out.println(info);
    }

}
