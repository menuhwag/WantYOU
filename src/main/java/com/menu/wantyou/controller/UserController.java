package com.menu.wantyou.controller;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController("/auth")
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
    public ResponseEntity<User> signUp(@RequestBody SignUpDTO signupDTO) throws DuplicateKeyException {
        return new ResponseEntity<>(userService.create(signupDTO), HttpStatus.CREATED);
    }

    @PostMapping(value = "/signin", produces = "application/json; charset=UTF-8")
    public ResponseEntity<JwtResponseDTO> signIn(@RequestBody SignInDTO signinDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signinDTO.getUsername(), signinDTO.getPassword()); // 인증용 객체 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JwtResponseDTO(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(HttpException exception) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(exception)
                , exception.getStatus());
    }
}
