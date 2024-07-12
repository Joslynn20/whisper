package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicatedFollowException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "이미 팔로우한 회원입니다.";

    public DuplicatedFollowException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
