package com.naown.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naown.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * JobMapper接口 用于数据交互
 * @USER: chenjian
 * @DATE: 2021/2/14 0:57 周日
 **/
public interface QuartzMapper extends BaseMapper<QuartzJob> {
}
