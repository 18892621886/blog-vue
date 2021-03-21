package com.naown.utils.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: chenjian
 * @since: 2021/3/14 14:15 周日
 **/
@ConfigurationProperties(prefix = "naown.email")
@Component
@Data
public class EmailConfig {
    /** 邮件服务器地址 */
    private String smtpHost;
    /** 发件人的用户名 */
    private String sendUserName;
    /** 发件人密码 */
    private String sendUserPassword;
    /** 收件人邮箱 */
    private String addressee;
}
