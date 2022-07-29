package com.menu.wantyou.service;

import com.menu.wantyou.domain.EmailVerifyToken;
import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.CreateProfileDTO;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.dto.UpdateUserDTO;
import com.menu.wantyou.lib.exception.ExistsValueException;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.lib.util.VerifyEmailSender;
import com.menu.wantyou.repository.EmailVerifyTokenRepository;
import com.menu.wantyou.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailVerifyTokenRepository emailVerifyTokenRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    private final String username = "jack01";
    private final String password = "passw0rd";
    private final String email = "jack01@gmail.com";
    private final String nickname = "jackson";
    private String name = "홍길동";
    private String birthYear = "2000";
    private String birthDay = "1223";

    private CreateProfileDTO createProfileDTO;
    private SignUpDTO signupDTO;
    private User user;
    private User savedUser;
    private Profile profile;

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {

        @BeforeEach
        public void setUp() {
            signupDTO = new SignUpDTO(username, password, email, nickname);
            createProfileDTO = CreateProfileDTO.builder()
                                                .name(name)
                                                .birthYear(birthYear)
                                                .birthDay(birthDay)
                                                .build();
            user = new User(signupDTO);
            profile = new Profile(createProfileDTO);
            user.setProfile(profile);
        }

        @Test
        @DisplayName("저장")
        void create() {
            //given
            given(passwordEncoder.encode(any(String.class))).willReturn(password);
            given(userRepository.existsByUsername(any(String.class))).willReturn(false);
            given(userRepository.existsByEmail(any(String.class))).willReturn(false);
            given(userRepository.save(any(User.class))).willReturn(user);
            given(emailVerifyTokenRepository.save(any(EmailVerifyToken.class))).willReturn(null);

            //when
            try (MockedStatic<VerifyEmailSender> mockedStatic = mockStatic(VerifyEmailSender.class)) {
                savedUser = userService.create(signupDTO, createProfileDTO);
            }

            //then
            assertEquals(user, savedUser);
            assertEquals(profile, savedUser.getProfile());
        }

        @Nested
        @DisplayName("중복체크")
        class ExistsKey {
            @Test
            @DisplayName("예외 발생:아이디 중복")
            void throwsExceptionWhenExistsUsername() {
                given(userRepository.existsByUsername(any(String.class))).willReturn(true);

                assertThrows(ExistsValueException.class, () -> userService.create(signupDTO, createProfileDTO));
            }

            @Test
            @DisplayName("예외 발생:이메일 중복")
            void throwsExceptionWhenExistsEmail() {
                given(userRepository.existsByEmail(any(String.class))).willReturn(true);

                assertThrows(ExistsValueException.class, () -> userService.create(signupDTO, createProfileDTO));
            }
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {
        String newPW = "12341234";
        String newEmail = "jack01@naver.com";
        String newNickname = "jacking";
        private UpdateUserDTO updateUserDTO;

        @BeforeEach
        public void setUp() {
            updateUserDTO = new UpdateUserDTO(newPW, newEmail, newNickname);
            signupDTO = new SignUpDTO(username, password, email, nickname);
            user = new User(signupDTO);
        }

        @Test
        @DisplayName("정상")
        public void success() {

            given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));
            given(passwordEncoder.encode(any(String.class))).willReturn(newPW);
            given(userRepository.save(any(User.class))).willReturn(user.update(updateUserDTO));

            User updateUser = userService.update(username, updateUserDTO);

            assertEquals(updateUser.getUsername(), user.getUsername());
            assertEquals(updateUser.getPassword(), newPW);
            assertEquals(updateUser.getNickname(), newNickname);
            assertEquals(updateUser.getEmail(), newEmail);
        }

        @Test
        @DisplayName("유저 정보 없을 시 NotFoundException 예외 발생")
        public void throwsNotFoundException() {
            given(userRepository.findByUsername(any(String.class))).willThrow(new NotFoundException("해당 유저 정보를 찾을 수 없습니다."));
            assertThrows(NotFoundException.class, () -> userService.update(username, updateUserDTO));
        }
    }
}