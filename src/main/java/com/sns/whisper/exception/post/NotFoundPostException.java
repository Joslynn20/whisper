package com.sns.whisper.exception.post;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundPostException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "게시물이 존재하지 않습니다.";

    public NotFoundPostException() {
        super(HTTP_STATUS, MESSAGE);
    }

}
