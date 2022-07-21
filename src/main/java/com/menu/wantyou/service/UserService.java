package com.menu.wantyou.service;

import com.menu.wantyou.domain.EmailVerifyToken;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.SignUpDTO;
import com.menu.wantyou.dto.UpdateUserDTO;
import com.menu.wantyou.lib.exception.EmailSendException;
import com.menu.wantyou.lib.exception.ExistsValueException;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.lib.util.VerifyEmailSender;
import com.menu.wantyou.repository.EmailVerifyTokenRepository;
import com.menu.wantyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailVerifyTokenRepository emailVerifyTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackOn= {EmailSendException.class, IllegalArgumentException.class})
    public User create(SignUpDTO signupDTO) throws ExistsValueException{
        String username = signupDTO.getUsername();
        String email = signupDTO.getEmail();

        if (checkExistsUsername(username) || checkExistsEmail(email)) {
            throw new ExistsValueException("이미 사용중인 아이디 또는 이메일입니다.");
        }

        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        User user = new User(signupDTO);
        User savedUser = userRepository.save(user);

        // 인증토큰 생성 및 이메일 전송
        String uuid = UUID.randomUUID().toString();
        EmailVerifyToken emailVerifyToken = new EmailVerifyToken(savedUser, uuid);
        emailVerifyTokenRepository.save(emailVerifyToken);

        VerifyEmailSender.sendVerifyCode(savedUser.getEmail(), uuid);

        return savedUser;
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

    @Transactional(rollbackOn = {NotFoundException.class, IllegalArgumentException.class})
    public void emailVerify(String token) {
        // verifyToken 검색 -> return User or username
        EmailVerifyToken emailVerify = emailVerifyTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("잘못된 인증정보입니다."));
        // 값이 있으면, return 값의 유저의 authEmail true 로 수정
        User user = emailVerify.getUser();
        user.setAuthEmail(true);
        userRepository.save(user);
        // 인증완료된 토큰 삭제
        emailVerifyTokenRepository.delete(emailVerify);
    }

    public boolean checkExistsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkExistsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
