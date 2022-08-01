package com.menu.wantyou.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.menu.wantyou.dto.UserDTO;
import com.menu.wantyou.dto.admin.AdminUpdateUserDTO;
import com.menu.wantyou.lib.enumeration.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

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

    @Size(min = 6, max = 16)
    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Size(min = 2, max = 10)
    @Column(nullable = false)
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @JsonIgnore
    @Column(nullable = false)
    private boolean enabled = true;

    @JsonIgnore
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

    public User(UserDTO.SignUp.CreateUser createUserDTO) {
        this.username = createUserDTO.getUsername();
        this.password = createUserDTO.getPassword();
        this.email = createUserDTO.getEmail();
        this.nickname = createUserDTO.getNickname();
    }

    public User update(UserDTO.Update updateUserDTO) {
        String password = updateUserDTO.getPassword();
        String email = updateUserDTO.getEmail();
        String nickname = updateUserDTO.getNickname();

        if (password != null) setPassword(password);
        if (email != null) setEmail(email);
        if (nickname != null) setNickname(nickname);

        return this;
    }

    public User update(AdminUpdateUserDTO updateUserDTO) {
        Boolean enabled = updateUserDTO.getEnabled();
        Role role = updateUserDTO.getRole();

        if (enabled != null) setEnabled(enabled);
        if (role != null) setRole(role);

        return this;
    }
}
