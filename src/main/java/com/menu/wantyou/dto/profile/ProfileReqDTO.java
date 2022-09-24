package com.menu.wantyou.dto.profile;

import com.menu.wantyou.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
public class ProfileReqDTO {
    private String name;

    @Size(min = 4, max = 4)
    private String birthYear;

    @Size(min = 4, max = 4)
    private String birthDay;

    private List<String> hobby;

    public Profile toEntity() {
        return Profile.builder()
                .name(this.name)
                .birthYear(this.birthYear)
                .birthDay(this.birthDay)
                .hobby(String.join(";", this.hobby))
                .build();
    }
}
