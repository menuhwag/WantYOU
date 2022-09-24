package com.menu.wantyou.domain;

import com.menu.wantyou.dto.profile.ProfileReqDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    private final String username = "jack01";
    private final String password = "passw0rd";
    private final String email = "jack01@gmail.com";
    private final String nickname = "jackson";
    private final String name = "홍길동";
    private final String year = "2000";
    private final String day = "0315";

    @Test
    public void Create_Profile() {
        ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                                                .name(name)
                                                .birthYear(year)
                                                .birthDay(day)
                                                .build();
        Profile profile = profileReqDTO.toEntity();

        assertNull(profile.getId());
        assertEquals(name, profile.getName());
        assertEquals(year, profile.getBirthYear());
        assertEquals(day, profile.getBirthDay());
        assertEquals(null, profile.getHobby());
    }

    @Test
    public void When_Creating_BirthDay_is_Wrong() {
        String year = "2000";
        String day1 = "1302";
        String day2 = "0230";

        ProfileReqDTO profileReqDTO1 = ProfileReqDTO.builder()
                                                    .name(name)
                                                    .birthYear(year)
                                                    .birthDay(day1)
                                                    .build();

        ProfileReqDTO profileReqDTO2 = ProfileReqDTO.builder()
                                                    .name(name)
                                                    .birthYear(year)
                                                    .birthDay(day2)
                                                    .build();

        assertThrows(IllegalArgumentException.class, profileReqDTO1::toEntity);
        assertThrows(IllegalArgumentException.class, profileReqDTO2::toEntity);
    }

    @Test
    public void Update_Profile() {
        ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                                                .name(name)
                                                .birthYear(year)
                                                .birthDay(day)
                                                .build();
        Profile profile = profileReqDTO.toEntity();
        String newName = "김첨지";
        String newYear = "2001";
        String newDay = "1009";
        List<String> newHobby = new ArrayList<>();
        newHobby.add("축구");
        newHobby.add("영화");

        ProfileReqDTO updateProfileReqDTO = ProfileReqDTO.builder()
                                                        .name(newName)
                                                        .birthYear(newYear)
                                                        .birthDay(newDay)
                                                        .hobby(newHobby)
                                                        .build();

        profile.updateName(updateProfileReqDTO.getName());
        profile.updateBirthYear(updateProfileReqDTO.getBirthYear());
        profile.updateBirthDay(updateProfileReqDTO.getBirthDay());
        profile.updateHobby(updateProfileReqDTO.getHobby());

        assertEquals(newName, profile.getName());
        assertEquals(newYear, profile.getBirthYear());
        assertEquals(newDay, profile.getBirthDay());
        assertEquals("축구;영화", profile.getHobby());
    }

    @Test
    public void When_Updating_BirthDay_is_Wrong() {
        ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                                                    .name(name)
                                                    .birthYear(year)
                                                    .birthDay(day)
                                                    .build();
        Profile profile = profileReqDTO.toEntity();
        String newYear = "2001";
        String newDay = "1309";

        ProfileReqDTO updateProfileDTO = ProfileReqDTO.builder()
                                                        .birthYear(newYear)
                                                        .birthDay(newDay)
                                                        .build();

        assertThrows(IllegalArgumentException.class, () -> {
            profile.updateBirthYear(updateProfileDTO.getBirthYear());
            profile.updateBirthDay(updateProfileDTO.getBirthDay());
        });
    }

    @Test
    public void nullHobbyUpdateTest() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("축구");
        hobbies.add("영화");

        ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                .name(name)
                .birthYear(year)
                .birthDay(day)
                .hobby(hobbies)
                .build();

        Profile profile = profileReqDTO.toEntity();

        String newYear = "2001";
        String newDay = "1209";

        ProfileReqDTO updateProfileDTO = ProfileReqDTO.builder()
                                                    .birthYear(newYear)
                                                    .birthDay(newDay)
                                                    .build();

        profile.updateName(updateProfileDTO.getName());
        profile.updateBirthYear(updateProfileDTO.getBirthYear());
        profile.updateBirthDay(updateProfileDTO.getBirthDay());
        profile.updateHobby(updateProfileDTO.getHobby());

        assertEquals(name, profile.getName());
        assertEquals(newYear, profile.getBirthYear());
        assertEquals(newDay, profile.getBirthDay());
        assertEquals(String.join(";", hobbies), profile.getHobby());
    }

    @Test
    public void emptyHobbyUpdateTest() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("축구");
        hobbies.add("영화");

        ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                .name(name)
                .birthYear(year)
                .birthDay(day)
                .hobby(hobbies)
                .build();

        Profile profile = profileReqDTO.toEntity();

        String newYear = "2001";
        String newDay = "1209";
        List<String> newHobbies = new ArrayList<>();

        ProfileReqDTO updateProfileDTO = ProfileReqDTO.builder()
                .birthYear(newYear)
                .birthDay(newDay)
                .hobby(newHobbies)
                .build();

        profile.updateName(updateProfileDTO.getName());
        profile.updateBirthYear(updateProfileDTO.getBirthYear());
        profile.updateBirthDay(updateProfileDTO.getBirthDay());
        profile.updateHobby(updateProfileDTO.getHobby());

        assertEquals(name, profile.getName());
        assertEquals(newYear, profile.getBirthYear());
        assertEquals(newDay, profile.getBirthDay());
        assertEquals("", profile.getHobby());
    }
}