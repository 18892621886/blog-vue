package com.naown.common.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 登录验证实体类
 * @author: chenjian
 * @since: 2021/3/1 23:08 周一
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginInfo implements Serializable {
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
}
