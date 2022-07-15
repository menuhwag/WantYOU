package com.menu.wantyou.controller;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.*;
import com.menu.wantyou.lib.enumeration.Key;
import com.menu.wantyou.lib.exception.*;
import com.menu.wantyou.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

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
        if (userService.confirmPassword(signinDTO)) {
            return new ResponseEntity<>(new JwtResponseDTO("ex_token", signinDTO.getUsername()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(HttpException exception) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(exception)
                , exception.getStatus());
    }
}
