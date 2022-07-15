package com.menu.wantyou.lib.exception;

import org.springframework.http.HttpStatus;

public class ExistsValueException extends HttpException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ExistsValueException(String message) {
        super(message, status);
    }
}
