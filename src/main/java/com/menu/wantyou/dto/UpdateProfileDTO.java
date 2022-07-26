package com.menu.wantyou.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    private String name;
    private String birthYear;
    private String birthDay;
    private String hobby;
}
