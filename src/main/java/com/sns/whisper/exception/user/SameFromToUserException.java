package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SameFromToUserException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "회원은 자신을 팔로우할 수 없습니다.";


    public SameFromToUserException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
