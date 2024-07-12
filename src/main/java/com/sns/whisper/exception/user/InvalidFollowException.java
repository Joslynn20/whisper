package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidFollowException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "존재하지 않는 팔로우 내역입니다.";

    public InvalidFollowException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
