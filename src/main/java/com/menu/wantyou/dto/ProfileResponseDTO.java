package com.menu.wantyou.dto;

import com.menu.wantyou.domain.Profile;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class ProfileResponseDTO {
    private String name;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private List<String> hobby;

    public ProfileResponseDTO(Profile profile) {
        this.name = profile.getName();
        this.birthYear = Integer.parseInt(profile.getBirthYear());
        this.birthMonth = Integer.parseInt(profile.getBirthDay().substring(0,2));
        this.birthDay = Integer.parseInt(profile.getBirthDay().substring(2));
        this.hobby = Arrays.asList(profile.getHobby().split(";"));
    }
}
