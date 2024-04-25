package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;


public class NotValidEmailFormatException extends BusinessException {

    private String errorCode;
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "잘못된 형식의 이메일입니다.";

    public NotValidEmailFormatException() {
        super(HTTP_STATUS, MESSAGE);
    }

}
