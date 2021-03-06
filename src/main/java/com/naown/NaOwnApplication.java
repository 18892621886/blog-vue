package com.naown;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenjian
 */
@SpringBootApplication
@MapperScan({"com.naown.aop.mapper","com.naown.quartz.mapper","com.naown.shiro.mapper","com.naown.common.mapper"})
public class NaOwnApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaOwnApplication.class, args);
    }

}
