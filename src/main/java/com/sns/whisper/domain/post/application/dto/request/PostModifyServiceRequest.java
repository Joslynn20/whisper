package com.sns.whisper.domain.post.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostModifyServiceRequest {

    private String userId;
    private String content;

    @Builder
    public PostModifyServiceRequest(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
