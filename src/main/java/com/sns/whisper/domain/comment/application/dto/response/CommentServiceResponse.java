package com.sns.whisper.domain.comment.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentServiceResponse {

    private Long id;
    private String profileImageUrl;
    private String authorUserId;
    private String content;

    @Builder
    public CommentServiceResponse(Long id, String profileImageUrl, String authorUserId,
            String content) {
        this.id = id;
        this.profileImageUrl = profileImageUrl;
        this.authorUserId = authorUserId;
        this.content = content;
    }
}
