package com.menu.wantyou.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.dto.UpdateUserDTO;
import com.menu.wantyou.lib.enumeration.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends TimestampsCreatedModified {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;

    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean enabeled = true;

    @Column(nullable = false)
    private boolean authEmail = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public User(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public User(SignUpDTO signUpDTO) {
        this.username = signUpDTO.getUsername();
        this.password = signUpDTO.getPassword();
        this.email = signUpDTO.getEmail();
        this.nickname = signUpDTO.getNickname();
    }

    public User update(UpdateUserDTO updateUserDTO) {
        String password = updateUserDTO.getPassword();
        String email = updateUserDTO.getEmail();
        String nickname = updateUserDTO.getNickname();

        if (password != null) setPassword(password);
        if (email != null) setEmail(email);
        if (nickname != null) setNickname(nickname);

        return this;
    }
}
