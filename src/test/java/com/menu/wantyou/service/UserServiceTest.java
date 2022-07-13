package com.menu.wantyou.service;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.SignInDTO;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    private final String username = "jack01";
    private final String password = "passw0rd";
    private final String email = "jack01@gmail.com";
    private final String nickname = "jackson";

    private SignUpDTO signupDTO;
    private SignInDTO signinDTO;
    private User user;
    private User savedUser;

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {

        @BeforeEach
        public void setUp() {
            signupDTO = new SignUpDTO(username, password, email, nickname);
            user = new User(username, password, email, nickname);
            given(passwordEncoder.encode(any(String.class))).willReturn(password);
        }

        @Test
        @DisplayName("저장")
        void create() {
            //given
            given(userRepository.existsByUsername(any(String.class))).willReturn(false);
            given(userRepository.existsByEmail(any(String.class))).willReturn(false);
            given(userRepository.save(any(User.class))).willReturn(user);

            //when
            savedUser = userService.create(signupDTO);

            //then
            assertEquals(user, savedUser);
        }

        @Nested
        @DisplayName("중복체크")
        class ExistsKey {
            @Test
            @DisplayName("예외 발생:아이디 중복")
            void throwsExceptionWhenExistsUsername() {
                given(userRepository.existsByUsername(any(String.class))).willReturn(true);

                assertThrows(DuplicateKeyException.class, () -> userService.create(signupDTO));
            }

            @Test
            @DisplayName("예외 발생:이메일 중복")
            void throwsExceptionWhenExistsEmail() {
                given(userRepository.existsByUsername(any(String.class))).willReturn(true);

                assertThrows(DuplicateKeyException.class, () -> userService.create(signupDTO));
            }
        }
    }

    @Nested
    @DisplayName("로그인")
    class SignIn {

        @BeforeEach
        public void setUp() {
            signupDTO = new SignUpDTO(username, password, email, nickname);
            signinDTO = new SignInDTO(username, password);
            user = new User(username, password, email, nickname);
            given(passwordEncoder.encode(any(String.class))).willReturn(password);
            given(userRepository.save(any(User.class))).willReturn(user);
            savedUser = userService.create(signupDTO);
        }

        @Test
        @DisplayName("해당 유저정보가 없을 시 UsernameNotFoundException 예외발생")
        void throwsUernameNotFoundException() {
            given(userRepository.findByUsername(any(String.class))).willReturn(Optional.ofNullable(null));

            assertThrows(UsernameNotFoundException.class, () -> userService.confirmPassword(signinDTO));
        }
        @Nested
        @DisplayName("비밀번호 확인")
        class ConfirmPassword {
            @Test
            @DisplayName("옳은 비밀번호 일시 true 반환")
            void rightPassword() {
                given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(savedUser));
                given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);

                assertTrue(userService.confirmPassword(signinDTO));

            }

            @Test
            @DisplayName("잘못된 비밀번호 일시 BadCredentialsException 예외발생")
            void throwsBadCredentialsException() {
                given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(savedUser));
                given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(false);

                assertThrows(BadCredentialsException.class, () -> userService.confirmPassword(signinDTO));
            }
        }

    }
}