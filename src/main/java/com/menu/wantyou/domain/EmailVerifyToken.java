package com.menu.wantyou.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "emailVerifyToken")
@Entity
public class EmailVerifyToken extends TimestampsCreated{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EmailVerifyToken(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
