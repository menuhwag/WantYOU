package com.menu.wantyou.lib.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {
    private static final HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(message, status);
    }
}
