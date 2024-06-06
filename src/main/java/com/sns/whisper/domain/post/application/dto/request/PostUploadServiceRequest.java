package com.sns.whisper.domain.post.application.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class PostUploadServiceRequest {

    private String content;
    private List<MultipartFile> images;


}
