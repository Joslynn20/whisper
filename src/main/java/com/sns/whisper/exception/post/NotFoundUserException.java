package com.sns.whisper.exception.post;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundUserException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "유효하지 않은 회원입니다.";

    public NotFoundUserException() {
        super(HTTP_STATUS, MESSAGE);
    }

}
