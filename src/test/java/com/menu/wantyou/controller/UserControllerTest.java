package com.menu.wantyou.controller;

import com.google.gson.Gson;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.dto.profile.ProfileReqDTO;
import com.menu.wantyou.dto.user.UserSignUpDTO;
import com.menu.wantyou.lib.exception.ExistsValueException;
import com.menu.wantyou.lib.util.jwt.JwtTokenProvider;
import com.menu.wantyou.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private final Gson gson = new Gson();
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Nested
    @DisplayName("/exists")
    class CheckExistsTest {
        @Nested
        @DisplayName("GET")
        class GetTest {
            @Nested
            @DisplayName("아이디")
            class UsernameTest {
                private final String key = "id";
                private final String value = "jack01";

                @Test
                @DisplayName("이미 존재할 시 checkExistsDTO.exists true 반환")
                void checkExistsTrue() throws Exception {
                    boolean exists = true;
                    given(userService.checkExistsUsername(any(String.class))).willReturn(exists);

                    mockMvc.perform(get("/auth/exists").param("key", key).param("value", value))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.key").exists())
                            .andExpect(jsonPath("$.value").exists())
                            .andExpect(jsonPath("$.exists").exists())
                            .andExpect(jsonPath("$.exists").value(exists));
                }

                @Test
                @DisplayName("존재하지 않을 시 checkExistsDTO.exists false 반환")
                void checkExistsFalse() throws Exception {
                    boolean exists = false;
                    given(userService.checkExistsUsername(any(String.class))).willReturn(exists);

                    mockMvc.perform(get("/auth/exists").param("key", key).param("value", value))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.key").exists())
                            .andExpect(jsonPath("$.value").exists())
                            .andExpect(jsonPath("$.exists").exists())
                            .andExpect(jsonPath("$.exists").value(exists));
                }
            }
            @Nested
            @DisplayName("이메일")
            class EmailTest {
                private final String key = "email";
                private final String value = "jack01@gmail.com";

                @Test
                @DisplayName("이미 존재할 시 checkExistsDTO.exists true 반환")
                void checkExistsTrue() throws Exception {
                    boolean exists = true;
                    given(userService.checkExistsEmail(any(String.class))).willReturn(exists);

                    mockMvc.perform(get("/auth/exists").param("key", key).param("value", value))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.key").exists())
                            .andExpect(jsonPath("$.value").exists())
                            .andExpect(jsonPath("$.exists").exists())
                            .andExpect(jsonPath("$.exists").value(exists));
                }

                @Test
                @DisplayName("존재하지 않을 시 checkExistsDTO.exists false 반환")
                void checkExistsFalse() throws Exception {
                    boolean exists = false;
                    given(userService.checkExistsEmail(any(String.class))).willReturn(exists);

                    mockMvc.perform(get("/auth/exists").param("key", key).param("value", value))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.key").exists())
                            .andExpect(jsonPath("$.value").exists())
                            .andExpect(jsonPath("$.exists").exists())
                            .andExpect(jsonPath("$.exists").value(exists));
                }
            }
            @Test
            @DisplayName("잘못된 키값 전달시 IllegalArgumentException 예외 발생")
            void trowsBadConstantException() throws Exception {
                String key = "nickname";
                String value = "jackson";

                mockMvc.perform(get("/auth/exists").param("key", key).param("value", value))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.timestamp").exists())
                        .andExpect(jsonPath("$.status").exists())
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.message").exists());
            }
        }
    }
    @Nested
    @DisplayName("/signup")
    class SignupTest {
        @Nested
        @DisplayName("POST")
        class PostTest {
            private final String username = "jack01";
            private final String password = "passw0rd";
            private final String email = "jack01@gmail.com";
            private final String nickname = "jackson";
            private final String name = "홍길동";
            private final String birthYear = "2000";
            private final String birthDay = "1223";

            private final UserSignUpDTO userSignUpDTO = UserSignUpDTO.builder()
                                                                .username(username)
                                                                .password(password)
                                                                .email(email)
                                                                .nickname(nickname)
                                                                .build();
            private final ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                                                                .name(name)
                                                                .birthYear(birthYear)
                                                                .birthDay(birthDay)
                                                                .build();
            private final SignUpDTO signUpDTO = SignUpDTO.builder()
                                                        .user(userSignUpDTO)
                                                        .profile(profileReqDTO)
                                                        .build();

            private final User user = signUpDTO.getUser().toEntity();

            String body = gson.toJson(signUpDTO);

            @Test
            @DisplayName("성공 시 ResponseEntity<UserResDTO> 반환")
            void createUser() throws Exception {
                given(userService.create(any(UserSignUpDTO.class), any(ProfileReqDTO.class))).willReturn(user);



                mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(body)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.username").exists())
                        .andExpect(jsonPath("$.email").exists())
                        .andExpect(jsonPath("$.nickname").exists());
            }

            @Test
            @DisplayName("유저정보 중복시 DuplicateKeyException 예외 발생")
            void existsKey() throws Exception {
                ExistsValueException exception = new ExistsValueException("이미 사용중인 아이디 또는 이메일입니다.");
                given(userService.create(any(UserSignUpDTO.class), any(ProfileReqDTO.class))).willThrow(exception);

                mockMvc.perform(
                                post("/auth/signup")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8")
                                        .content(body)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.timestamp").exists())
                        .andExpect(jsonPath("$.status").exists())
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.message").exists());
            }
        }
    }
}