package com.example.spellingcheck.exception;

import lombok.Getter;

//@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private final ExceptionCode exceptionCode;
    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
