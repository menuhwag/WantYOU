package com.menu.wantyou.dto;

import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ValidationErrorResponseDTO {
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
    private int status = 400;
    private String message = "값이 올바르지 않습니다.";
    private List<Map<String, Object>> error = new ArrayList<>();

    public ValidationErrorResponseDTO(MethodArgumentNotValidException exception) {
        exception.getFieldErrors().forEach(fieldError -> {
            Map<String, Object> errorMsg = new HashMap<>();
            errorMsg.put(fieldError.getField(), fieldError.getDefaultMessage());
            this.error.add(errorMsg);
        });
    }
}
