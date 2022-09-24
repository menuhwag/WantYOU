package com.menu.wantyou.dto;

import com.menu.wantyou.dto.profile.ProfileReqDTO;
import com.menu.wantyou.dto.user.UserSignUpDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    private UserSignUpDTO user;
    private ProfileReqDTO profile;
}
