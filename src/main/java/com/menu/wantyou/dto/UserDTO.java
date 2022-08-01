package com.menu.wantyou.dto;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignIn {
        @NotBlank
        @Size(min = 6, max = 16)
        private String username;

        @NotBlank
        @Size(min = 8, max = 16)
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignUp {
        @NotBlank
        @Size(min = 6, max = 16)
        private String username;

        @NotBlank
        @Size(min = 8, max = 16)
        private String password;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 2, max = 10)
        private String nickname;

        private String name;

        @Size(min = 4, max = 4)
        private String birthYear;

        @Size(min = 4, max = 4)
        private String birthDay;

        @Builder.Default
        private String hobby = "";

        public CreateUser toCreateUserDTO() {
            return CreateUser.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .nickname(this.nickname)
                    .build();
        }

        public CreateProfile toCreateProfileDTO() {
            return CreateProfile.builder()
                    .name(this.name)
                    .birthYear(this.birthYear)
                    .birthDay(this.birthDay)
                    .build();
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class CreateUser {
            @NotBlank
            @Size(min = 6, max = 16)
            private String username;

            @NotBlank
            @Size(min = 8, max = 16)
            private String password;

            @NotBlank
            @Email
            private String email;

            @NotBlank
            @Size(min = 2, max = 10)
            private String nickname;

            public User toEntity() {
                return new User(this);
            }
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class CreateProfile {
            private String name;

            @Size(min = 4, max = 4)
            private String birthYear;

            @Size(min = 4, max = 4)
            private String birthDay;

            private List<String> hobby;

            public Profile toEntity() {
                return new Profile(this);
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @Size(min = 8, max = 16)
        private String password;
        @Email
        private String email;
        @Size(min = 2, max = 10)
        private String nickname;
    }

    @Getter
    public static class Response {
        private String username;
        private String email;
        private String nickname;

        public Response(User user) {
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
        }
    }
}
