package com.naown.shiro.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @USER: chenjian
 * @DATE: 2021/2/21 15:13 周日
 **/
@Data
public class LoginDTO implements Serializable {
    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "用户密码不能为空")
    private String password;
}
