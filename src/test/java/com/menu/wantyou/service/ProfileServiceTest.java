package com.menu.wantyou.service;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.UserDTO;
import com.menu.wantyou.dto.ProfileDTO;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.repository.ProfileRepository;
import com.menu.wantyou.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private final String username = "test01";
    private final String password = "12341234";
    private final String email = "test01@test.com";
    private final String nickname = "tester";
    private final String name = "홍길동";
    private final String birthYear = "2000";
    private final String birthDay = "1223";
    private final List<String> updateHobby = new ArrayList<>();
    private final String updateName = "김첨지";

    private User user;

    @BeforeEach
    public void setUp() {
        updateHobby.add("영화");
        UserDTO.SignUp mockSignUpDTO = UserDTO.SignUp.builder()
                                            .username(username)
                                            .password(password)
                                            .email(email)
                                            .nickname(nickname)
                                            .name(name)
                                            .birthYear(birthYear)
                                            .birthDay(birthDay)
                                            .build();

        Profile mockProfile = mockSignUpDTO.toCreateProfileDTO().toEntity();
        user = mockSignUpDTO.toCreateUserDTO().toEntity();
        user.setProfile(mockProfile);
    }

    @Test
    public void find_A_Profile_By_Username() {
        //given
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));
        //when
        Profile findProfile = profileService.findByUsername(username);
        //then
        assertEquals(name, findProfile.getName());
        assertEquals(birthYear, findProfile.getBirthYear());
        assertEquals(birthDay, findProfile.getBirthDay());
        assertEquals(null, findProfile.getHobby());
    }

    @Test
    public void when_Can_Not_Find_A_User_Throw_NotFoundException() {
        given(userRepository.findByUsername(any(String.class))).willThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> profileService.findByUsername(username));
    }

    @Test
    public void update_Profile() {
        //given
        ProfileDTO.Update mockUpdateProfileDTO = ProfileDTO.Update.builder()
                                                                .name(updateName)
                                                                .hobby(updateHobby)
                                                                .build();
        //when
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        Profile result = profileService.update(username, mockUpdateProfileDTO);
        //then
        assertEquals(birthYear, result.getBirthYear());
        assertEquals(birthDay, result.getBirthDay());
        assertEquals(updateName, result.getName());
        assertEquals("영화", result.getHobby());
    }
}