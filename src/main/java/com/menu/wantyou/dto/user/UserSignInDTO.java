package com.menu.wantyou.dto.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
public class UserSignInDTO {
    @NotBlank
    @Size(min = 6, max = 16)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
}
