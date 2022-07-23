package com.menu.wantyou.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileDTO {
    private String name;
    private String year;
    private String day;
    private String hobby;
}
