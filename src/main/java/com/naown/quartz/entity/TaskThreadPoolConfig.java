package com.naown.quartz.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: chenjian
 * @DATE: 2021/2/23 22:30 周二
 **/
@ConfigurationProperties(prefix = "task.pool")
@Component
@Data
public class TaskThreadPoolConfig {
    /** 核心线程池大小 */
    private int corePoolSize;
    /** 最大线程数 */
    private int maxPoolSize;
    /** 活跃时间 */
    private int keepAliveSeconds;
    /** 队列容量 */
    private int queueCapacity;
}
