package com.zozak.word_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MessageType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "User not found"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Refresh token not found"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials"),
    INVALID_API_KEY(HttpStatus.BAD_REQUEST.value(), "Invalid api key"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST.value(),"Invalid refresh token"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(),"Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(),"Expired token"),
    INVALID_PLAN(HttpStatus.BAD_REQUEST.value(),"Invalid plan"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Something went wrong"),
    INVALID_USAGE(HttpStatus.BAD_REQUEST.value(),"Invalid usage");

    private final Integer status;
    private final String message;
}
