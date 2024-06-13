package com.sns.whisper.exception.user;

import com.sns.whisper.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class FileUploadException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE = "파일 업로드에 실패했습니다.";

    public FileUploadException() {
        super(HTTP_STATUS, MESSAGE);
    }
}
