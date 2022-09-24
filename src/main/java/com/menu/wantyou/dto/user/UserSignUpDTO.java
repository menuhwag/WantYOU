package com.menu.wantyou.dto.user;

import com.menu.wantyou.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
public class UserSignUpDTO {
    @NotBlank
    @Size(min = 6, max = 16)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 10)
    private String nickname;

    public void setPassword(String encodedPW) {
        this.password = encodedPW;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
