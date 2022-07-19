package com.menu.wantyou.service;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.SignInDTO;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.dto.UpdateUserDTO;
import com.menu.wantyou.lib.exception.ExistsValueException;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.lib.exception.UnauthorizedException;
import com.menu.wantyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(SignUpDTO signupDTO) throws ExistsValueException{
        String username = signupDTO.getUsername();
        String email = signupDTO.getEmail();

        if (checkExistsUsername(username) || checkExistsEmail(email)) {
            throw new ExistsValueException("이미 사용중인 아이디 또는 이메일입니다.");
        }

        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        User user = new User(signupDTO);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOneByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Not Found the User"));
    }

    public User update(String username,UpdateUserDTO updateUserDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저 정보를 찾을 수 없습니다."));
        if (updateUserDTO.getPassword() != null) updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        user.update(updateUserDTO);
        return userRepository.save(user);
    }

    public void delete(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저 정보를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    public boolean checkExistsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkExistsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
