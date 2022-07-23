package com.menu.wantyou.config;

import com.menu.wantyou.lib.util.jwt.JwtAccessDeniedHandler;
import com.menu.wantyou.lib.util.jwt.JwtAuthenticationEntryPoint;
import com.menu.wantyou.lib.util.jwt.JwtTokenProvider;
import com.menu.wantyou.security.CustomAuthenticationEntryPoint;
import com.menu.wantyou.security.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/exists", "/auth/signin", "/auth/signup", "/auth/email-verify").permitAll()
                .antMatchers("/auth").hasAuthority("ADMIN")
                .anyRequest().authenticated();
        http.apply(new JwtSecurityConfig(jwtTokenProvider));
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
