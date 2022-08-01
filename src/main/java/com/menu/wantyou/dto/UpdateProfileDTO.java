package com.menu.wantyou.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UpdateProfileDTO {
    private String name;

    @Size(min = 4, max = 4)
    private String birthYear;

    @Size(min = 4, max = 4)
    private String birthDay;

    private List<String> hobby;
}
