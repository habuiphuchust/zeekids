package com.example.spellingcheck.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ValidationException validationException, WebRequest webRequest) {
        log.error("Error Validation occurred.", validationException);
        String code = ExceptionCode.MB_WEB_INVALID_PARAM.getCode();
        String message = getValidationMessage(validationException);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .path(webRequest.getDescription(false))
                .errorCode(code)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);

    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> handleCinemaException(CustomException customException, WebRequest webRequest) {
        log.error("handleCustomException: " + customException.getMessage());
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(customException.getExceptionCode().getMessage())
                .path(webRequest.getDescription(false))
                .errorCode(customException.getExceptionCode().getCode())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException authenticationException, WebRequest webRequest) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(authenticationException.getMessage())
                .path(webRequest.getDescription(false))
                .errorCode(HttpStatus.UNAUTHORIZED.toString())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception exception, WebRequest webRequest) {
        log.error("Error Normal occurred", exception);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(webRequest.getDescription(false))
                .errorCode(ExceptionCode.INTERNAL_SERVER_ERROR.getCode())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    private String getValidationMessage(ValidationException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.error(fieldError.getObjectName());
            log.error(fieldError.getField());
            log.error((String) fieldError.getRejectedValue());
            log.error(fieldError.getDefaultMessage());
        }
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            return fieldErrors.get(0).getDefaultMessage();
        }
        return null;
    }
}
