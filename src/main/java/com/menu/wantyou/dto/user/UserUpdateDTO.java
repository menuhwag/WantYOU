package com.menu.wantyou.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @Size(min = 8, max = 16)
    private String password;
    @Email
    private String email;
    @Size(min = 2, max = 10)
    private String nickname;
}
