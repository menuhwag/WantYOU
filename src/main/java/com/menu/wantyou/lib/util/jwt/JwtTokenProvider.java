package com.menu.wantyou.lib.util.jwt;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.domain.UserDetailsImpl;
import com.menu.wantyou.lib.enumeration.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final String secret;
    private final long tokenExpiration;
    private final String AUTHORITIES_KEY = "auth";


    public JwtTokenProvider(@Value("${util.jwt.secret}") String secret, @Value("${util.jwt.tokenExpiration}") long tokenExpiration) {
        this.secret = secret;
        this.tokenExpiration = tokenExpiration * 1000;
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();
        Date expiration = new Date(now + tokenExpiration);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(expiration)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.SignatureException | MalformedJwtException exception) {
            // 잘못된 jwt signature
        } catch (io.jsonwebtoken.ExpiredJwtException exception) {
            // jwt 만료
        } catch (io.jsonwebtoken.UnsupportedJwtException exception) {
            // 지원하지 않는 jwt
        } catch (IllegalArgumentException exception) {
            // 잘못된 jwt 토큰
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        String rolesStr = claims.get(AUTHORITIES_KEY).toString();

        User user = new User();
        user.setUsername(username);
        user.setPassword(token);
        user.setRole(Role.of(rolesStr));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
