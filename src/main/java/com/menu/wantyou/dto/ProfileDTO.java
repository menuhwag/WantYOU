package com.menu.wantyou.dto;

import com.menu.wantyou.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

public class ProfileDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Update {
        private String name;

        @Size(min = 4, max = 4)
        private String birthYear;

        @Size(min = 4, max = 4)
        private String birthDay;

        private List<String> hobby;
    }

    @Getter
    public static class Response {
        private String name;
        private int birthYear;
        private int birthMonth;
        private int birthDay;
        private List<String> hobby;

        public Response(Profile profile) {
            this.name = profile.getName();
            this.birthYear = Integer.parseInt(profile.getBirthYear());
            this.birthMonth = Integer.parseInt(profile.getBirthDay().substring(0,2));
            this.birthDay = Integer.parseInt(profile.getBirthDay().substring(2));
            if (profile.getHobby() != null) this.hobby = Arrays.asList(profile.getHobby().split(";"));
        }
    }
}
