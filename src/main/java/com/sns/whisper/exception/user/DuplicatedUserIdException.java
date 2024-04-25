package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicatedUserIdException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "중복된 아이디입니다.";

    public DuplicatedUserIdException() {
        super(HTTP_STATUS, MESSAGE);
    }

}
