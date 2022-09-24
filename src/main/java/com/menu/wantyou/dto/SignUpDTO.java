package com.menu.wantyou.dto;

import com.menu.wantyou.dto.profile.ProfileReqDTO;
import com.menu.wantyou.dto.user.UserSignUpDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpDTO {
    private UserSignUpDTO user;
    private ProfileReqDTO profile;
}
