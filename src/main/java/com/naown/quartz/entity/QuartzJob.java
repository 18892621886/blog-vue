package com.naown.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 对应sys_quartz表
 * @author chenjian
 * @DATE: 2021/2/13 20:29 周六
 **/
@Data
@TableName("sys_quartz")
public class QuartzJob implements Serializable {

    /**
     * 任务调度参数key
     */
    @TableField(exist = false)
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /** JobId */
    @TableId(value = "job_id",type = IdType.AUTO)
    private Long id;

    /** 定时器名称 */
    private String jobName;

    /** 方法名称 */
    private String methodName;

    /** 参数 */
    private String params;

    /** Bean名字 */
    private String beanName;

    /** cron表达式 */
    private String cronExpression;

    /** 状态，暂停或启动 1:启动 0:暂停 */
    private Boolean status;

    /** 负责人 */
    private String personInCharge;

    /** 报警邮箱 */
    private String email;

    /** 备注 */
    private String description;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

}
