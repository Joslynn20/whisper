package com.sns.whisper.exception.post;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedUserException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    private static final String MESSAGE = "로그인 후 이용 가능합니다.";

    public NotAuthorizedUserException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
