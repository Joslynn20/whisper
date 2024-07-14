package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotValidUserException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "유효하지 않은 회원입니다.";

    public NotValidUserException() {
        super(HTTP_STATUS, MESSAGE);
    }

}
