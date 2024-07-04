package com.sns.whisper.domain.post.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostModifyServiceRequest {

    private Long postId;
    private String userId;
    private String content;

    @Builder
    public PostModifyServiceRequest(Long postId, String userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
