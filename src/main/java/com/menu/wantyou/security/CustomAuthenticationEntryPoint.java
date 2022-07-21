package com.menu.wantyou.security;

import com.menu.wantyou.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        int status = 401;
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String message = authException.getMessage();

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(status, error, message);

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(status);
        response.getWriter().println(errorResponse);
    }
}
