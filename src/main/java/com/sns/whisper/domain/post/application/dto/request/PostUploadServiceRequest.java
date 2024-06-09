package com.sns.whisper.domain.post.application.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostUploadServiceRequest {

    private String content;
    private List<MultipartFile> images;
    private String userId;

    @Builder
    public PostUploadServiceRequest(String content, List<MultipartFile> images, String userId) {
        this.content = content;
        this.images = images;
        this.userId = userId;
    }
}
