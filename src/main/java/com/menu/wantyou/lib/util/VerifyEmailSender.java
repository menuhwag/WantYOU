package com.menu.wantyou.lib.util;

import com.menu.wantyou.lib.exception.EmailSendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class VerifyEmailSender {
    @Value("${util.email.user}")
    private static String user;
    @Value("${util.email.password}")
    private static String pass;
    @Value(("${base.url}"))
    private static String baseURL;

    public VerifyEmailSender(
            @Value("${util.email.user}") String user,
            @Value("${util.email.password}") String pass ) {
        this.user = user;
        this.pass = pass;
    }

    private static final Properties prop;
    private static final Session session;

    static {
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.naver.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.naver.com");

        session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
    }

    public static void sendVerifyCode(String email, String verifyToken) {
        MimeMessage message = new MimeMessage(session);
        String url = baseURL + "/auth/email-verify?verifyToken=" + verifyToken;
        String html = "가입확인 버튼를 누르시면 가입 인증이 완료됩니다.<br/>"
                + "<form action=\"" + url + "\" method=\"POST\">"
                + "<button>가입확인</button>"
                + "</form>";
        try {
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("WantYOU 이메일 인증", "utf-8");
            message.setText(html, "utf-8", "html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new EmailSendException();
        }
    }
}
