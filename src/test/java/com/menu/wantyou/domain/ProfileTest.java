package com.menu.wantyou.domain;

import com.menu.wantyou.dto.UpdateProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    private String username = "jack01";
    private String password = "passw0rd";
    private String email = "jack01@gmail.com";
    private String nickname = "jackson";
    private String name = "홍길동";
    private String year = "2000";
    private String day = "0315";

    User user = new User(username, password, email, nickname);
    @Test
    public void Create_Profile() {
        Profile profile = new Profile(user, name, year, day);

        assertNull(profile.getId());
        assertEquals(user, profile.getUser());
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

        assertThrows(IllegalArgumentException.class, () -> new Profile(user, name, year, day1));
        assertThrows(IllegalArgumentException.class, () -> new Profile(user, name, year, day2));
    }

    @Test
    public void Update_Profile() {
        Profile profile = new Profile(user, name, year, day);
        String newName = "김첨지";
        String newYear = "2001";
        String newDay = "1009";
        String newHobby = "축구;영화";

        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
        updateProfileDTO.setName(newName);
        updateProfileDTO.setYear(newYear);
        updateProfileDTO.setDay(newDay);
        updateProfileDTO.setHobby(newHobby);

        Profile updateProfile =  profile.update(updateProfileDTO);

        assertEquals(profile.getId(), updateProfile.getId());
        assertEquals(profile.getUser(), updateProfile.getUser());
        assertEquals(newName, updateProfile.getName());
        assertEquals(newYear, updateProfile.getBirthYear());
        assertEquals(newDay, updateProfile.getBirthDay());
        assertEquals(newHobby, updateProfile.getHobby());
    }

    @Test
    public void When_Updating_BirthDay_is_Wrong() {
        Profile profile = new Profile(user, name, year, day);
        String newYear = "2001";
        String newDay = "1309";

        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
        updateProfileDTO.setYear(newYear);
        updateProfileDTO.setDay(newDay);

        assertThrows(IllegalArgumentException.class, () -> profile.update(updateProfileDTO));
    }
}