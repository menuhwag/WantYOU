package com.menu.wantyou.lib.exception;

import org.springframework.http.HttpStatus;

public class BadConstantException extends HttpException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public BadConstantException(String message) {
        super(message, status);
    }
}
