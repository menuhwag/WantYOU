package com.menu.wantyou.controller;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.*;
import com.menu.wantyou.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/exists", produces = "application/json; charset=UTF-8")
    public ResponseEntity<CheckExistsDTO> checkExists(@RequestParam("key") String key, @RequestParam("value") String value) {
        CheckExistsDTO checkExistsDTO = new CheckExistsDTO(key, value);
        if ("id".equals(key)) {
            checkExistsDTO.setExists(userService.checkExistsUsername(value));
        } else if ("email".equals(key)) {
            checkExistsDTO.setExists(userService.checkExistsEmail(value));
        }
        return new ResponseEntity<>(checkExistsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/signup", produces = "application/json; charset=UTF-8")
    public ResponseEntity<User> signUp(@RequestBody SignUpDTO signupDTO) throws DuplicateKeyException {
        return new ResponseEntity<>(userService.create(signupDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/signin", produces = "application/json; charset=UTF-8")
    public ResponseEntity<JwtResponseDTO> signIn(@RequestBody SignInDTO signinDTO) {
        if (userService.confirmPassword(signinDTO)) {
            return new ResponseEntity<>(new JwtResponseDTO("ex_token", signinDTO.getUsername()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Todo. 커스텀 예외 핸들러로 교체
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateKeyException(DuplicateKeyException exception) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(400, "Bad Request", exception.getMessage())
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(404, "Not Found", exception.getMessage())
                , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException exception) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(403, "Forbidden", exception.getMessage())
                , HttpStatus.FORBIDDEN);
    }
}
