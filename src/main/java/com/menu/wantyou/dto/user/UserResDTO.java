package com.menu.wantyou.dto.user;

import com.menu.wantyou.domain.User;
import lombok.Getter;

@Getter
public class UserResDTO {
    private final String username;
    private final String email;
    private final String nickname;

    public UserResDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
