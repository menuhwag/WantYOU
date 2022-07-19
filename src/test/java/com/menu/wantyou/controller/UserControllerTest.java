package com.menu.wantyou.controller;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.*;
import com.menu.wantyou.lib.exception.BadConstantException;
import com.menu.wantyou.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Nested
    @DisplayName("/exists")
    class CheckExists {
        @Nested
        @DisplayName("GET")
        class Get {
            @Nested
            @DisplayName("아이디")
            class Username {
                private final String key = "id";
                private final String value = "jack01";

                @Test
                @DisplayName("이미 존재할 시 checkExistsDTO.exists true 반환")
                void checkExistsTrue() {
                    boolean exists = true;
                    given(userService.checkExistsUsername(any(String.class))).willReturn(exists);

                    ResponseEntity<CheckExistsDTO> responseEntity = userController.checkExists(key, value);

                    CheckExistsDTO responseBody = responseEntity.getBody();
                    assertEquals(key.toUpperCase(), responseBody.getKey().name());
                    assertEquals(value, responseBody.getValue());
                    assertEquals(exists, responseBody.isExists());
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                }

                @Test
                @DisplayName("존재하지 않을 시 checkExistsDTO.exists false 반환")
                void checkExistsFalse() {
                    boolean exists = false;
                    given(userService.checkExistsUsername(any(String.class))).willReturn(exists);

                    ResponseEntity<CheckExistsDTO> responseEntity = userController.checkExists(key, value);

                    CheckExistsDTO responseBody = responseEntity.getBody();
                    assertEquals(key.toUpperCase(), responseBody.getKey().name());
                    assertEquals(value, responseBody.getValue());
                    assertEquals(exists, responseBody.isExists());
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                }
            }
            @Nested
            @DisplayName("이메일")
            class Email {
                private final String key = "email";
                private final String value = "jack01@gmail.com";

                @Test
                @DisplayName("이미 존재할 시 checkExistsDTO.exists true 반환")
                void checkExistsTrue() {
                    boolean exists = true;
                    given(userService.checkExistsEmail(any(String.class))).willReturn(exists);

                    ResponseEntity<CheckExistsDTO> responseEntity = userController.checkExists(key, value);

                    CheckExistsDTO responseBody = responseEntity.getBody();
                    assertEquals(key.toUpperCase(), responseBody.getKey().name());
                    assertEquals(value, responseBody.getValue());
                    assertEquals(exists, responseBody.isExists());
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                }

                @Test
                @DisplayName("존재하지 않을 시 checkExistsDTO.exists false 반환")
                void checkExistsFalse() {
                    boolean exists = false;
                    given(userService.checkExistsEmail(any(String.class))).willReturn(exists);

                    ResponseEntity<CheckExistsDTO> responseEntity = userController.checkExists(key, value);

                    CheckExistsDTO responseBody = responseEntity.getBody();
                    assertEquals(key.toUpperCase(), responseBody.getKey().name());
                    assertEquals(value, responseBody.getValue());
                    assertEquals(exists, responseBody.isExists());
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                }
            }
            @Test
            @DisplayName("잘못된 키값 전달시 IllegalArgumentException 예외 발생")
            void trowsBadConstantException() {
                String key = "nickname";
                String value = "jackson";

                assertThrows(BadConstantException.class, () -> userController.checkExists(key, value));
            }
        }
    }
    @Nested
    @DisplayName("/signup")
    class Signup {
        @Nested
        @DisplayName("POST")
        class Post {
            private final String username = "jack01";
            private final String password = "passw0rd";
            private final String email = "jack01@gmail.com";
            private final String nickname = "jackson";

            private final SignUpDTO signUpDTO = new SignUpDTO(username, password, email, nickname);
            private final User user = new User(username, password, email, nickname);

            @Test
            @DisplayName("성공 시 ResponseEntity<User> 반환")
            void createUser() {
                ResponseEntity<User> responseEntity = new ResponseEntity<>(user, HttpStatus.CREATED);
                given(userService.create(any(SignUpDTO.class))).willReturn(user);

                ResponseEntity<User> result = userController.signUp(signUpDTO);

                assertEquals(responseEntity, result);
                assertEquals(user, result.getBody());
            }

            @Test
            @DisplayName("유저정보 중복시 DuplicateKeyException 예외 발생")
            void existsKey() {
                int status = 400;
                String error = "Bad Request";
                DuplicateKeyException exception = new DuplicateKeyException("이미 사용중인 아이디 또는 이메일입니다.");
                given(userService.create(any(SignUpDTO.class))).willThrow(exception);

                assertThrows(exception.getClass(), () -> userController.signUp(signUpDTO));
            }
        }
    }
}