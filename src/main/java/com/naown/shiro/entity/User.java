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
 * 对应sys_user表
 * @USER: chenjian
 * @DATE: 2021/2/20 21:55 周六
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {
    /** ID */
    @TableId(value = "user_id")
    private Long id;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickName;

    /** 性别 */
    private String gender;

    /** 手机号码 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像路径 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 是否为admin账号 1:是 0:否 */
    private Boolean adminStatus;

    /** 状态：1启用、0禁用 */
    private Boolean enabled;

    /** 随机盐 */
    private String salt;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 修改密码的时间 */
    private Timestamp pwdResetTime;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 角色权限集合 */
    @TableField(exist = false)
    private List<Role> roles;
}
