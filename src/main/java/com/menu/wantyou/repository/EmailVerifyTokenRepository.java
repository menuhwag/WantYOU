package com.menu.wantyou.repository;

import com.menu.wantyou.domain.EmailVerifyToken;
import com.menu.wantyou.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerifyTokenRepository extends JpaRepository<EmailVerifyToken, Long> {
    Optional<EmailVerifyToken> findByToken(String token);
    Optional<EmailVerifyToken> findByUser(User user);
    boolean existsByUser(User user);
}
