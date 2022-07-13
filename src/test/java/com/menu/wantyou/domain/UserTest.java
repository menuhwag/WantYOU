package com.menu.wantyou.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Domain Test")
class UserTest {
    @Test
    public void createUser() {
        String username = "jack01";
        String password = "passw0rd";
        String email = "jack01@gmail.com";
        String nickname = "jackson";

        User user = new User(username, password, email, nickname);

        assertNull(user.getId());
        assertEquals(user.getRole(), "USER");
        assertTrue(user.isEnabeled());
        assertEquals(user.getUsername(), username);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getNickname(), nickname);
    }
}