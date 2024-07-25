package com.sns.whisper.domain.post.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDeleteServiceRequest {

    private Long postId;
    private String userId;

    @Builder
    public PostDeleteServiceRequest(Long postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
