package com.sns.whisper.exception.post;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class FileDeleteException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE = "파일 삭제에 실패했습니다.";

    public FileDeleteException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
