package com.sns.whisper.exception;

import static java.util.Objects.requireNonNull;

import com.sns.whisper.global.dto.FailResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Status : {}, Message : {}";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponseDto> methodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        String message = requireNonNull(
                requireNonNull(e.getFieldError())
                        .getDefaultMessage());

        log.warn(LOG_FORMAT, e.getClass()
                              .getSimpleName(), e.getStatusCode(), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new FailResponseDto(message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<FailResponseDto> businessException(BusinessException e) {
        HttpStatus httpStatus = e.getHttpStatus();
        String message = e.getMessage();

        log.warn(
                LOG_FORMAT,
                e.getClass()
                 .getSimpleName(),
                e.getHttpStatus(),
                message
        );

        return ResponseEntity
                .status(httpStatus.value())
                .body(new FailResponseDto(message));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<FailResponseDto> dataAccessException(DataAccessException e) {
        log.error(
                LOG_FORMAT,
                e.getClass()
                 .getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailResponseDto("Internal Server Error"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FailResponseDto> runtimeException(RuntimeException e) {
        log.error(
                LOG_FORMAT,
                e.getClass()
                 .getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailResponseDto("Internal Server Error"));
    }
}
