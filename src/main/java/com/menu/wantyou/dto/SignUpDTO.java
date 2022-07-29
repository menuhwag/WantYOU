package com.menu.wantyou.dto;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class SignUpDTO {
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

    public CreateUserDTO toCreateUserDTO() {
        return CreateUserDTO.builder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .nickname(this.nickname)
                .build();
    }

    public CreateProfileDTO toCreateProfileDTO() {
        return CreateProfileDTO.builder()
                .name(this.name)
                .birthYear(this.birthYear)
                .birthDay(this.birthDay)
                .build();
    }

    @Data
    @Builder
    public static class CreateUserDTO {
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

    @Data
    @Builder
    public static class CreateProfileDTO {
        private String name;

        @Size(min = 4, max = 4)
        private String birthYear;

        @Size(min = 4, max = 4)
        private String birthDay;

        @Builder.Default
        private String hobby = "";

        public Profile toEntity() {
            return new Profile(this);
        }
    }
}
