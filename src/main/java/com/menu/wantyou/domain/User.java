package com.menu.wantyou.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.menu.wantyou.lib.enumeration.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
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
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @JsonIgnore
    @Builder.Default
    @Column(nullable = false)
    private boolean authEmail = false;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
