package com.menu.wantyou.repository;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.dto.SignUpDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProfileRepositoryTest {
    private final String name = "홍길동";
    private final String birthYear = "1999";
    private final String birthDay = "0516";

    @Autowired
    private ProfileRepository profileRepository;

    private Profile savedProfile;

    @BeforeEach
    public void setUp() {
        SignUpDTO.CreateProfileDTO createProfileDTO = SignUpDTO.CreateProfileDTO.builder()
                                                                                .name(name)
                                                                                .birthYear(birthYear)
                                                                                .birthDay(birthDay)
                                                                                .build();

        Profile profile = createProfileDTO.toEntity();
        savedProfile = profileRepository.save(profile);
    }

    @Test
    public void create_Profile() {
        assertNotNull(savedProfile.getId());
        assertEquals(name, savedProfile.getName());
        assertEquals(birthYear, savedProfile.getBirthYear());
        assertEquals(birthDay, savedProfile.getBirthDay());
        assertEquals("", savedProfile.getHobby());
    }

    @Test
    public void find_By_Id() {
        Profile findProfile = profileRepository.findById(savedProfile.getId()).get();

        assertNotNull(findProfile);
        assertEquals(savedProfile, findProfile);
    }

    @Test
    public void delete_Profile() {
        Long savedProfileId = savedProfile.getId();

        Profile profile = profileRepository.findById(savedProfileId).get();

        profileRepository.delete(profile);

        Optional<Profile> findProfile = profileRepository.findById(savedProfileId);
        assertEquals(Optional.empty(), findProfile);
    }
}