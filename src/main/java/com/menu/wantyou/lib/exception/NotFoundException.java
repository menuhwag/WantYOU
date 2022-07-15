package com.menu.wantyou.lib.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException{
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message, status);
    }
}
