package com.menu.wantyou.service;

import com.menu.wantyou.domain.EmailVerifyToken;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.ChangeVerifyEmailDTO;
import com.menu.wantyou.dto.admin.AdminUpdateUserDTO;
import com.menu.wantyou.dto.profile.ProfileReqDTO;
import com.menu.wantyou.dto.user.UserSignUpDTO;
import com.menu.wantyou.dto.user.UserUpdateDTO;
import com.menu.wantyou.lib.exception.EmailSendException;
import com.menu.wantyou.lib.exception.ExistsValueException;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.lib.exception.UnauthorizedException;
import com.menu.wantyou.lib.util.VerifyEmailSender;
import com.menu.wantyou.repository.EmailVerifyTokenRepository;
import com.menu.wantyou.repository.UserRepository;
import com.sun.jdi.InternalException;
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
    public User create(UserSignUpDTO createUserDTO, ProfileReqDTO createProfileDTO) throws ExistsValueException{
        String username = createUserDTO.getUsername();
        String email = createUserDTO.getEmail();

        if (checkExistsUsername(username) || checkExistsEmail(email)) {
            throw new ExistsValueException("이미 사용중인 아이디 또는 이메일입니다.");
        }

        createUserDTO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        User user = createUserDTO.toEntity();
        user.setProfile(createProfileDTO.toEntity());
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

    @Transactional
    public User update(String username, UserUpdateDTO updateUserDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저 정보를 찾을 수 없습니다."));
        user.updatePassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        user.updateEmail(updateUserDTO.getEmail());
        user.updateNickname(updateUserDTO.getNickname());
        return user;
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
        // 인증완료된 토큰 삭제
        emailVerifyTokenRepository.delete(emailVerify);
    }

    @Transactional(rollbackOn = {EmailSendException.class, InternalException.class, IllegalArgumentException.class})
    public void changeVerifyEmailAndSendVerifyMail(ChangeVerifyEmailDTO changeVerifyEmailDTO) {
        String username = changeVerifyEmailDTO.getUsername();
        String password = changeVerifyEmailDTO.getPassword();
        String email = changeVerifyEmailDTO.getEmail();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저 정보를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(password, user.getPassword())) throw new UnauthorizedException("비밀번호를 확인해주세요.");

        user.setEmail(email);

        String uuid = UUID.randomUUID().toString();
        EmailVerifyToken emailVerifyToken;
        if (emailVerifyTokenRepository.existsByUser(user)) {
            emailVerifyToken = emailVerifyTokenRepository.findByUser(user).orElseThrow(InternalException::new);
            emailVerifyToken.setToken(uuid);
        } else {
            emailVerifyToken = new EmailVerifyToken(user, uuid);
            emailVerifyTokenRepository.save(emailVerifyToken);
        }

        VerifyEmailSender.sendVerifyCode(email, uuid);
    }

    @Transactional
    public User updateEnableAndRole(AdminUpdateUserDTO adminUpdateUserDTO) {
        String username = adminUpdateUserDTO.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저 정보를 찾을 수 없습니다."));
        if (adminUpdateUserDTO.getEnabled() != null) user.setEnabled(adminUpdateUserDTO.getEnabled());
        if (adminUpdateUserDTO.getRole() != null) user.setRole(adminUpdateUserDTO.getRole());
        return user;
    }

    public boolean checkExistsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkExistsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
