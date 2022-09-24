package com.menu.wantyou.controller;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.domain.UserDetailsImpl;
import com.menu.wantyou.dto.*;
import com.menu.wantyou.dto.admin.AdminUpdateUserDTO;
import com.menu.wantyou.dto.user.UserResDTO;
import com.menu.wantyou.dto.user.UserSignInDTO;
import com.menu.wantyou.dto.user.UserUpdateDTO;
import com.menu.wantyou.lib.enumeration.Key;
import com.menu.wantyou.lib.exception.*;
import com.menu.wantyou.lib.util.jwt.JwtFilter;
import com.menu.wantyou.lib.util.jwt.JwtTokenProvider;
import com.menu.wantyou.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping(value = "/exists", produces = "application/json; charset=UTF-8")
    public ResponseEntity<CheckExistsDTO> checkExists(@RequestParam("key") String key, @RequestParam("value") String value) {
        CheckExistsDTO checkExistsDTO = new CheckExistsDTO(Key.titleOf(key), value);
        if ("id".equals(key)) {
            checkExistsDTO.setExists(userService.checkExistsUsername(value));
        } else if ("email".equals(key)) {
            checkExistsDTO.setExists(userService.checkExistsEmail(value));
        }
        return new ResponseEntity<>(checkExistsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/signup", produces = "application/json; charset=UTF-8")
    public ResponseEntity<UserResDTO> signUp(@Valid @RequestBody SignUpDTO signupDTO) throws DuplicateKeyException {
        return ResponseEntity.status(201)
                .body(new UserResDTO(userService.create(signupDTO.getUser(), signupDTO.getProfile())));
    }

    @PostMapping(value = "/signin", produces = "application/json; charset=UTF-8")
    public ResponseEntity<JwtResponseDTO> signIn(@Valid @RequestBody UserSignInDTO signinDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signinDTO.getUsername(), signinDTO.getPassword()); // 인증용 객체 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity.status(200)
                .headers(httpHeaders)
                .body(new JwtResponseDTO(jwt));
    }

    @GetMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity<UserResDTO> getMyAuth(@AuthenticationPrincipal UserDetailsImpl myAuth) {
        return ResponseEntity.status(200)
                .body(new UserResDTO(userService.findOneByUsername(myAuth.getUsername())));
    }

    @PatchMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity<UserResDTO> updateMyAuth(@AuthenticationPrincipal UserDetailsImpl myAuth, @Valid @RequestBody UserUpdateDTO updateUserDTO) {
        return ResponseEntity.status(200)
                .body(new UserResDTO(userService.update(myAuth.getUsername(), updateUserDTO)));
    }

    @DeleteMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity deleteMyAuth(@AuthenticationPrincipal UserDetailsImpl myAuth) {
        userService.delete(myAuth.getUsername());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/email-verify", produces = "application/json; charset=UTF-8")
    public void verifyEmail(@RequestParam("verifyToken") String verifyToken) {
        userService.emailVerify(verifyToken);
    }

    @PatchMapping(value = "/email-verify", produces = "application/json; charset=UTF-8")
    public ResponseEntity changeVerifyEmailAndSendVerifyMail(@Valid @RequestBody ChangeVerifyEmailDTO changeVerifyEmailDTO) {
        userService.changeVerifyEmailAndSendVerifyMail(changeVerifyEmailDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<UserResDTO>> findAll() {
        List<UserResDTO> userResponse = new ArrayList<>();
        List<User> users = userService.findAll();
        users.forEach(user -> {
            userResponse.add(new UserResDTO(user));
        });
        return ResponseEntity.status(200)
                .body(userResponse);
    }

    @PatchMapping(value = "", produces = "application/json; charset=UTF-8")
    public ResponseEntity<UserResDTO> updateEnableAndRole(@Valid @RequestBody AdminUpdateUserDTO adminUpdateUserDTO) {
        return ResponseEntity.status(200)
                .body(new UserResDTO(userService.updateEnableAndRole(adminUpdateUserDTO)));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(HttpException exception) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(exception),
                exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationError(MethodArgumentNotValidException exception) {
        System.out.println(exception.getClass());
        return new ResponseEntity<>(
                new ValidationErrorResponseDTO(exception),
                HttpStatus.BAD_REQUEST
        );
    }
}
