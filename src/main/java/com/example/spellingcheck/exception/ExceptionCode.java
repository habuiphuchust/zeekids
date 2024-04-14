package com.example.spellingcheck.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    //4xx
    REFRESH_TOKEN_NOT_FOUND("400", "Token not found"),
    UNAUTHORIZED("401", "Unauthorized"),
    USERNAME_OR_PASSWORD_INCORRECT("401", "Username or password incorrect"),
    INVALID_JWT_TOKEN("403", "Invalid JWT token"),
    EXPIRED_JWT_TOKEN("403", "Expired JWT token"),
    UNSUPPORTED_JWT_TOKEN("403", "Unsupported JWT token"),
    JWT_EMPTY("403", "JWT claims string is empty"),
    EXPIRED_REFRESH_TOKEN("403", "Expired Refresh Token"),
    USERNAME_ALREADY_EXIST("409", "Username Already Exist"),
    USER_NOT_FOUND("400", "User Not Found"),
    MB_WEB_INVALID_PARAM("400", "Invalid Param"),

    //5xx
    INTERNAL_SERVER_ERROR("500", "Internal server error");
    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
