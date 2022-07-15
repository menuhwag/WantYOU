package com.menu.wantyou.service;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.SignInDTO;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.lib.exception.ExistsValueException;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.lib.exception.UnauthorizedException;
import com.menu.wantyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(SignUpDTO signupDTO) throws ExistsValueException{
        String username = signupDTO.getUsername();
        String password = passwordEncoder.encode(signupDTO.getPassword());
        String email = signupDTO.getEmail();
        String nickname = signupDTO.getNickname();

        if (checkExistsUsername(username) || checkExistsEmail(email)) {
            throw new ExistsValueException("이미 사용중인 아이디 또는 이메일입니다.");
        }

        User user = new User(username, password, email, nickname);
        return userRepository.save(user);
    }

    public boolean confirmPassword(SignInDTO signinDTO) throws NotFoundException, UnauthorizedException {
        String username = signinDTO.getUsername();
        String password = signinDTO.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저정보를 찾을 수 없습니다."));
        String encodePassword = user.getPassword();
        if (passwordEncoder.matches(password, encodePassword)) {
            return true;
        } else {
            throw new UnauthorizedException("비밀번호를 확인해주세요.");
        }
    }

    public boolean checkExistsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkExistsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
