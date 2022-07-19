package com.menu.wantyou.domain;

import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.dto.UpdateUserDTO;
import com.menu.wantyou.lib.enumeration.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Domain Test")
class UserTest {
    private String username = "jack01";
    private String password = "passw0rd";
    private String email = "jack01@gmail.com";
    private String nickname = "jackson";

    @Test
    public void createUser() {
        User user = new User(username, password, email, nickname);

        assertNull(user.getId());
        assertEquals(user.getRole(), Role.USER);
        assertTrue(user.isEnabeled());
        assertEquals(user.getUsername(), username);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getNickname(), nickname);
    }
    @Test
    public void updateUser() {
        User user = new User(username, password, email, nickname);

        String update_password = "password";
        String update_email = "jack01@naver.com";

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setPassword(update_password);
        updateUserDTO.setEmail(update_email);
        user.update(updateUserDTO);

        assertNull(user.getId());
        assertEquals(user.getUsername(), username);
        assertEquals(user.getPassword(), update_password);
        assertEquals(user.getEmail(), update_email);
        assertEquals(user.getNickname(), nickname);
    }
}