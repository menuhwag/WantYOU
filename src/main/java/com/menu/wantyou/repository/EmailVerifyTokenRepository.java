package com.menu.wantyou.repository;

import com.menu.wantyou.domain.EmailVerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerifyTokenRepository extends JpaRepository<EmailVerifyToken, Long> {
    Optional<EmailVerifyToken> findByToken(String token);
}
