package com.menu.wantyou.dto;

import com.menu.wantyou.lib.exception.HttpException;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ErrorResponseDTO {
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
    private int status;
    private String error;
    private String message;

    public ErrorResponseDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponseDTO(HttpException exception) {
        this.status = exception.getStatusCode();
        this.error = exception.getError();
        this.message = exception.getMessage();
    }
}
