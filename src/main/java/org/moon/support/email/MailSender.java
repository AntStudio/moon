package org.moon.support.email;

import org.moon.core.spring.config.annotation.Config;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送
 * @author:Gavin
 * @date 2014/12/12 0012
 */
@Component
public class MailSender {

    @Config("mail.user")
    private String mailUser;

    @Config("mail.password")
    private String mailPassword;

    @Config("mail.host")
    private String mailHost;

    /**
     * 邮件发送
     * @param receiver 接收邮箱
     * @param subject 主题
     * @param content 内容
     * @throws javax.mail.MessagingException
     */
    public void send(String receiver,String subject,String content) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", mailHost);
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser,mailPassword);
            }
        });

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailUser));
        InternetAddress[] address = {new InternetAddress(receiver)};
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(content);
        Transport.send(msg);
    }
}
