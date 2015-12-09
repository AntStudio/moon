package org.moon.support.email;

import org.moon.core.spring.config.annotation.Config;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
     * @throws MessagingException
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
        String[] arr = receiver.split("，|,");
        InternetAddress[] address = new InternetAddress[arr.length];
        for (int i = 0; i < arr.length; i++) {
            address[i] = new InternetAddress(arr[i]);
        }
        msg.addRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(content,"text/html;charset=utf-8");
        Transport.send(msg);
    }


    public static void main(String[] args) throws MessagingException {
        MailSender m = new MailSender();
        m.send("734978618@qq.com","测试","<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "\t<meta charset=\"utf-8\"/>\n" +
            "\t<title>\t情况汇总</title>\n" +
            "\t<style>\n" +
            "\t.table{\n" +
            "\t\tborder-collapse: collapse;\n" +
            "\t}\n" +
            "\t.table th,.table td{\n" +
            "\t\tborder:solid 1px #DDD;\n" +
            "\t\tpadding:10px;\n" +
            "\t}\n" +
            "\n" +
            "\t.row{\n" +
            "\t\tpadding:10px 0px;\n" +
            "\t}\n" +
            "\n" +
            "\t.red{\n" +
            "\t\tcolor:#f00;\n" +
            "\t}\n" +
            "\n" +
            "\t.green{\n" +
            "\t\tcolor:#00B809;\n" +
            "\t}\n" +
            "\n" +
            "\t.content{\n" +
            "\t\tfont-size: 16px;\n" +
            "\t\tpadding:20px;\n" +
            "\t\tfont-family: 'Helvetica Neue', Helvetica, 'Microsoft Yahei', 'Hiragino Sans GB', 'WenQuanYi Micro Hei', sans-serif;\n" +
            "\t}\n" +
            "\n" +
            "\ta{\n" +
            "\t\tcolor: #428bca;\n" +
            "\t\ttext-decoration: none;\n" +
            "\t}\n" +
            "\n" +
            "\t.description{\n" +
            "\t\tcolor: #CCC;\n" +
            "\t}\n" +
            "\n" +
            "\t.user-statistics{\n" +
            "\t\tborder-left: solid 5px #FF5252;\n" +
            "\t\tpadding-left: 9px;\n" +
            "\t}\n" +
            "\n" +
            "\t.sms-statistics{\n" +
            "\t\tborder-left: solid 5px #57B0F0;\n" +
            "\t\tpadding-left: 9px;\n" +
            "\t}\n" +
            "\n" +
            "\t.article-statistics{\n" +
            "\t\tborder-left: solid 5px #00B62C;\n" +
            "\t\tpadding-left: 9px;\n" +
            "\t}\n" +
            "\t</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\t<div class=\"content\">\n" +
            "\t\t<h1 class=\"user-statistics\">用户统计</h1>\n" +
            "\t\t<div class=\"row\">目前注册人数总计：<span class=\"red\">299</span>人，认证医生人数：<span class=\"red\">299</span>人，认证比例：<span class=\"red\">68%</span></div>\n" +
            "\t\t<div class=\"row\">本月注册人数：<span class=\"red\">50</span>人，认证医生人数：<span class=\"red\">299</span>人，认证比例：<span class=\"red\">68%</span></div>\n" +
            "\t\t<div>\n" +
            "\t\t\t<div class=\"row\">本周注册情况：</div>\n" +
            "\t\t\t<table class=\"table\">\n" +
            "\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<th>周一</th>\n" +
            "\t\t\t\t\t\t<th>周二</th>\n" +
            "\t\t\t\t\t\t<th>周三</th>\n" +
            "\t\t\t\t\t\t<th>周四</th>\n" +
            "\t\t\t\t\t\t<th>周五</th>\n" +
            "\t\t\t\t\t\t<th>周六</th>\n" +
            "\t\t\t\t\t\t<th>周天</th>\n" +
            "\t\t\t\t</tr>\n" +
            "\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<th>12</th>\n" +
            "\t\t\t\t\t\t<th>2</th>\n" +
            "\t\t\t\t\t\t<th>32</th>\n" +
            "\t\t\t\t\t\t<th>1</th>\n" +
            "\t\t\t\t\t\t<th>44</th>\n" +
            "\t\t\t\t\t\t<th>23</th>\n" +
            "\t\t\t\t\t\t<th>11</th>\n" +
            "\t\t\t\t</tr>\n" +
            "\t\t\t</table>\n" +
            "\t\t\t<div class=\"row\">本周共注册：<span class=\"red\">50</span>人，较上周30注册人数，上涨<span class=\"red\">20%</span></div>\n" +
            "\t\t\t<div class=\"row\">本周共认证：<span class=\"red\">50</span>人，较上周30认证人数，上涨<span class=\"red\">20%</span></div>\n" +
            "\t\t\t<div class=\"row\">本周注册比例<span class=\"red\">50</span>人，较上周50%注册b比例，下降<span class=\"green\">20%</span></div>\n" +
            "\t\t</div>\n" +
            "\n" +
            "\t\t<h1 class=\"sms-statistics\">短信统计</h1>\n" +
            "\t\t<div class=\"row\">截止今天，共发送了xxx条验证短信，其中本周发送了xxx条验证短信，账户余额：￥xx，预计还可用xx周.[请尽快充值，已保证验证短信可用性。]</div>\n" +
            "\n" +
            "\t\t<h1 class=\"article-statistics\">文章阅读统计</h1>\n" +
            "\t\t<div class=\"row\">目前阅读量前十的文章：</div>\n" +
            "\t\t<table class=\"table\">\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<th>标题</th>\n" +
            "\t\t\t\t\t<th>阅读量</th>\n" +
            "\t\t\t</tr>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<td>右室流出道憩室起源室性早搏消融治疗</td>\n" +
            "\t\t\t\t\t<td>60</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<td>xxxxxxxxxxxx</td>\n" +
            "\t\t\t\t\t<td>60</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<td>xxxxxxxxxxxx</td>\n" +
            "\t\t\t\t\t<td>60</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<td>xxxxxxxxxxxx</td>\n" +
            "\t\t\t\t\t<td>60</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<td>xxxxxxxxxxxx</td>\n" +
            "\t\t\t\t\t<td>60</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t\t<td>xxxxxxxxxxxx</td>\n" +
            "\t\t\t\t\t<td>60</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t</table>\n" +
            "\t\t<div class=\"row\">文章阅读详情请登录系统查看：<a href=\"http://app.xheart.cn/statistics/articleReading.html\" target=\"_blank\">立即登录查看</a></div>\n" +
            "\n" +
            "\t\t<div class=\"row description\">注：本邮件由系统自动发出，如不愿接受此类邮件，请直接回复本邮件，主题为：退订系统统计邮件。</div>\n" +
            "\t</div>\n" +
            "</body>\n" +
            "</html>");
    }

}
