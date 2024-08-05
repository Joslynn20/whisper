package com.sns.whisper.exception.post;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PostNotBelongToUserException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    private static final String MESSAGE = "현재 회원이 작성한 글이 아닙니다.";

    public PostNotBelongToUserException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
