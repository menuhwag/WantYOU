package com.menu.wantyou.lib.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:/application-test.properties")
@SpringBootTest
class VerifyEmailSenderTest {
    @Value("${test.user.email}")
    private String user;

    @Value("${test.user.password}")
    private String password;

    @Value("${test.recipient.email}")
    private String email;

    @Value("${test.baseURL}")
    private String baseURL;

    private VerifyEmailSender verifyEmailSender = new VerifyEmailSender(user, password, baseURL);

    @Disabled
    @Test
    void sendVerifyCode() {
        verifyEmailSender.sendVerifyCode(email, "testcode");
    }
}