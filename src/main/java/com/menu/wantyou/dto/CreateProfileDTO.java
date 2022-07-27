package com.menu.wantyou.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class CreateProfileDTO {
    private String name;
    private String birthYear;
    private String birthDay;
    @Builder.Default private String hobby = "";
}
