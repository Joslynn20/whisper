package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class LoginFailException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "아이디와 비밀번호를 확인해주세요.";


    public LoginFailException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
