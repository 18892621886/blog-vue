package com.naown.aop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 对应sys_log表
 * @USER: chenjian
 * @DATE: 2021/2/21 1:30 周日
 **/
@Data
@TableName(value = "sys_log")
public class LogEntity {
    /** logId */
    @TableId(value = "log_id",type = IdType.AUTO)
    private Long id;

    /** 操作用户 */
    private String username;

    /** 描述 */
    private String description;

    /** 方法名 */
    private String method;

    /** 参数 */
    private String params;

    /** 日志类型 */
    private String logType;

    /** 请求ip */
    private String requestIp;

    /** 地址 */
    private String address;

    /** 浏览器  */
    private String browser;

    /** 请求耗时 */
    private Long time;

    /** 异常详细  */
    private byte[] exceptionDetail;

    /** 创建日期 */
    private Timestamp createTime;

    /**
     * 构造Log实体只需要先传入类型和执行时间，后续属性在进行赋值添加
     * @param logType
     * @param time
     */
    public LogEntity(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
