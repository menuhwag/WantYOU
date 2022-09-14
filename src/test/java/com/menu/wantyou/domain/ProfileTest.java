package com.menu.wantyou.domain;

import com.menu.wantyou.dto.UserDTO;
import com.menu.wantyou.dto.ProfileDTO;
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
        UserDTO.SignUp.CreateProfile createProfileDTO = UserDTO.SignUp.CreateProfile.builder()
                                                                                .name(name)
                                                                                .birthYear(year)
                                                                                .birthDay(day)
                                                                                .build();
        Profile profile = createProfileDTO.toEntity();

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

        UserDTO.SignUp.CreateProfile createProfileDTO1 = UserDTO.SignUp.CreateProfile.builder()
                                                                                .name(name)
                                                                                .birthYear(year)
                                                                                .birthDay(day1)
                                                                                .build();

        UserDTO.SignUp.CreateProfile createProfileDTO2 = UserDTO.SignUp.CreateProfile.builder()
                                                                                .name(name)
                                                                                .birthYear(year)
                                                                                .birthDay(day2)
                                                                                .build();

        assertThrows(IllegalArgumentException.class, createProfileDTO1::toEntity);
        assertThrows(IllegalArgumentException.class, createProfileDTO2::toEntity);
    }

    @Test
    public void Update_Profile() {
        UserDTO.SignUp.CreateProfile createProfileDTO = UserDTO.SignUp.CreateProfile.builder()
                                                                                .name(name)
                                                                                .birthYear(year)
                                                                                .birthDay(day)
                                                                                .build();
        Profile profile = createProfileDTO.toEntity();
        String newName = "김첨지";
        String newYear = "2001";
        String newDay = "1009";
        List<String> newHobby = new ArrayList<>();
        newHobby.add("축구");
        newHobby.add("영화");

        ProfileDTO.Update updateProfileDTO = ProfileDTO.Update.builder()
                                                            .name(newName)
                                                            .birthYear(newYear)
                                                            .birthDay(newDay)
                                                            .hobby(newHobby)
                                                            .build();

        profile.updateName(updateProfileDTO.getName());
        profile.updateBirthYear(updateProfileDTO.getBirthYear());
        profile.updateBirthDay(updateProfileDTO.getBirthDay());
        profile.updateHobby(updateProfileDTO.getHobby());

        assertEquals(newName, profile.getName());
        assertEquals(newYear, profile.getBirthYear());
        assertEquals(newDay, profile.getBirthDay());
        assertEquals("축구;영화", profile.getHobby());
    }

    @Test
    public void When_Updating_BirthDay_is_Wrong() {
        UserDTO.SignUp.CreateProfile createProfileDTO = UserDTO.SignUp.CreateProfile.builder()
                                                                                .name(name)
                                                                                .birthYear(year)
                                                                                .birthDay(day)
                                                                                .build();
        Profile profile = createProfileDTO.toEntity();
        String newYear = "2001";
        String newDay = "1309";

        ProfileDTO.Update updateProfileDTO = ProfileDTO.Update.builder()
                                                            .birthYear(newYear)
                                                            .birthDay(newDay)
                                                            .build();

        assertThrows(IllegalArgumentException.class, () -> {
            profile.updateBirthYear(updateProfileDTO.getBirthYear());
            profile.updateBirthDay(updateProfileDTO.getBirthDay());
        });
    }
}