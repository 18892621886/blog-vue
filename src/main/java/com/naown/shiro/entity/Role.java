package com.naown.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 对应sys_role表
 * @USER: chenjian
 * @DATE: 2021/2/20 22:17 周六
 **/
@TableName(value = "sys_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Role implements Serializable {
    /** ID */
    @TableId(value = "role_id")
    private Long id;

    /** 名称:超级管理员或者普通用户 */
    private String name;

    /** 角色级别 1 > 2 */
    private  Integer level;

    /** 描述 */
    private String description;

    /** 数据权限 */
    private String dataScope;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 权限用户集合 */
    @TableField(exist = false)
    private List<User> users;
}
