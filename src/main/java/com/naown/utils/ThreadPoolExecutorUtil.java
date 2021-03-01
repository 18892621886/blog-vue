package com.naown.utils;

import com.naown.quartz.entity.TaskThreadPoolConfig;
import com.naown.quartz.entity.TheadFactoryName;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: chenjian
 * @DATE: 2021/2/23 22:32 周二
 **/
public class ThreadPoolExecutorUtil {
    public static ThreadPoolExecutor getPoll(){
        TaskThreadPoolConfig properties = SpringContextUtils.getBean(TaskThreadPoolConfig.class);
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(properties.getQueueCapacity()),
                new TheadFactoryName()
        );
    }
}
