package com.naown.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author chenjian
 * @DATE: 2021/2/23 21:48 周二
 **/
@Data
@TableName("sys_quartz_log")
public class QuartzLog {
    /** ID */
    @TableId(value = "log_id",type = IdType.AUTO)
    private Long id;

    /** 任务名称 */
    private String jobName;

    /** bean名称 */
    private String beanName;

    /** 方法名称 */
    private String methodName;

    /** 参数 */
    private String params;

    /** cron表达式 */
    private String cronExpression;

    /** 状态，暂停或启动 1:启动 0:暂停 */
    private Boolean successStatus;

    /** 异常详情 */
    private String exceptionDetail;

    /** 执行耗时 */
    private Long time;

    /** 创建时间 */
    private Timestamp createTime;
}
