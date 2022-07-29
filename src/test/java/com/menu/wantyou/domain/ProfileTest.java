package com.menu.wantyou.domain;

import com.menu.wantyou.dto.CreateProfileDTO;
import com.menu.wantyou.dto.UpdateProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    private final String username = "jack01";
    private final String password = "passw0rd";
    private final String email = "jack01@gmail.com";
    private final String nickname = "jackson";
    private final String name = "홍길동";
    private final String year = "2000";
    private final String day = "0315";

    User user = new User(username, password, email, nickname);
    @Test
    public void Create_Profile() {
        CreateProfileDTO createProfileDTO = CreateProfileDTO.builder()
                                                            .name(name)
                                                            .birthYear(year)
                                                            .birthDay(day)
                                                            .build();
        Profile profile = new Profile(createProfileDTO);

        assertNull(profile.getId());
        assertEquals(name, profile.getName());
        assertEquals(year, profile.getBirthYear());
        assertEquals(day, profile.getBirthDay());
        assertEquals("", profile.getHobby());
    }

    @Test
    public void When_Creating_BirthDay_is_Wrong() {
        String year = "2000";
        String day1 = "1302";
        String day2 = "0230";

        CreateProfileDTO createProfileDTO1 = CreateProfileDTO.builder()
                                                            .name(name)
                                                            .birthYear(year)
                                                            .birthDay(day1)
                                                            .build();

        CreateProfileDTO createProfileDTO2 = CreateProfileDTO.builder()
                                                            .name(name)
                                                            .birthYear(year)
                                                            .birthDay(day2)
                                                            .build();

        assertThrows(IllegalArgumentException.class, () -> new Profile(createProfileDTO1));
        assertThrows(IllegalArgumentException.class, () -> new Profile(createProfileDTO2));
    }

    @Test
    public void Update_Profile() {
        CreateProfileDTO createProfileDTO = CreateProfileDTO.builder()
                                                            .name(name)
                                                            .birthYear(year)
                                                            .birthDay(day)
                                                            .build();
        Profile profile = new Profile(createProfileDTO);
        String newName = "김첨지";
        String newYear = "2001";
        String newDay = "1009";
        String newHobby = "축구;영화";

        UpdateProfileDTO updateProfileDTO = UpdateProfileDTO.builder()
                                                            .name(newName)
                                                            .birthYear(newYear)
                                                            .birthDay(newDay)
                                                            .hobby(newHobby)
                                                            .build();

        Profile updateProfile =  profile.update(updateProfileDTO);

        assertEquals(profile.getId(), updateProfile.getId());
        assertEquals(newName, updateProfile.getName());
        assertEquals(newYear, updateProfile.getBirthYear());
        assertEquals(newDay, updateProfile.getBirthDay());
        assertEquals(newHobby, updateProfile.getHobby());
    }

    @Test
    public void When_Updating_BirthDay_is_Wrong() {
        CreateProfileDTO createProfileDTO = CreateProfileDTO.builder()
                                                            .name(name)
                                                            .birthYear(year)
                                                            .birthDay(day)
                                                            .build();
        Profile profile = new Profile(createProfileDTO);
        String newYear = "2001";
        String newDay = "1309";

        UpdateProfileDTO updateProfileDTO = UpdateProfileDTO.builder()
                                                            .birthYear(newYear)
                                                            .birthDay(newDay)
                                                            .build();

        assertThrows(IllegalArgumentException.class, () -> profile.update(updateProfileDTO));
    }
}