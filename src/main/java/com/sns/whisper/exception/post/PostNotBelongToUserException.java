package com.sns.whisper.exception.post;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PostNotBelongToUserException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    private static final String MESSAGE = "게시물을 수정할 수 없습니다.";

    public PostNotBelongToUserException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
