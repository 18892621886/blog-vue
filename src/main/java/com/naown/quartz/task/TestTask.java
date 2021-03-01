package com.naown.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 任务实例，需要定时开启哪些任务需要在这个方法里面写逻辑
 * BeanName比如TestTask数据库中就是testTask因为spring默认首位字母小写或者自己制定名字
 * 方法如果有参数数据库则需要添加否则报错
 */
@Component
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void run(String params){
		logger.info("TestTask定时任务正在执行，参数为：{}", params);
	}
}