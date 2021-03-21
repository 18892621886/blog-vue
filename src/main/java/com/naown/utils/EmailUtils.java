package com.naown.utils;

import com.naown.quartz.entity.TaskThreadPoolConfig;
import com.naown.utils.entity.EmailConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 简单的邮箱工具类
 * @author: chenjian
 * @since: 2021/3/14 13:33 周日
 **/
public class EmailUtils {

    public static void sendEmail() throws Exception {
        // 需要注意的是bean不能new出来，必须从spring中获取
        EmailConfig emailConfig = SpringContextUtils.getBean(EmailConfig.class);
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.sohu.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(false);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        ts.connect(emailConfig.getSmtpHost(), emailConfig.getSendUserName(), emailConfig.getSendUserPassword());
        //4、创建邮件 简单的文字邮件
        Message message = createSimpleMail(session,emailConfig.getSendUserName(),emailConfig.getAddressee());
        // Message message = createImageMail(session); 带有图片的邮件
        // Message message = createAttachMail(session);带文件的邮件
        // Message message = createMixedMail(session); 带文字和附件的邮件 需要请参考email这个包，在java文件夹下
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }

    /**
     * 发送一封简单的邮件
     * @param session email的session
     * @param sendUserName 发送人邮箱
     * @param addressee 收件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createSimpleMail(Session session,String sendUserName, String addressee)
            throws Exception {
        String username = "小肥肥";
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(sendUserName));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(addressee));
        //邮件的标题
        message.setSubject("只包含文本的简单邮件");
        //邮件的文本内容
        message.setContent("<div>\n" +
                "\t<p>亲爱的<b>"+username+"</b>, 欢迎加入 biezhi!</p>\n" +
                "  \t<p>当您收到这封信的时候，您已经可以正常登录了。</p>\n" +
                "  \t<p>请点击链接登录首页: ${url}</p>\n" +
                "  \t<p>如果您的 email 程序不支持链接点击，请将上面的地址拷贝至您的浏览器(如IE)的地址栏进入。</p>\n" +
                "  \t<p>如果您还想申请管理员权限，可以联系管理员 ${email}</p>\n" +
                "  \t<p>我们对您产生的不便，深表歉意。</p>\n" +
                "  \t<p>希望您在 biezhi 系统度过快乐的时光!</p>\n" +
                "  \t<p></p>\n" +
                "  \t<p>-----------------------</p>\n" +
                "  \t<p></p>\n" +
                "  \t<p>(这是一封自动产生的email，请勿回复。)</p>\n" +
                "</div>", "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
}
