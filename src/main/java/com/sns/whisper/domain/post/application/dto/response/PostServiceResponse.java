package com.sns.whisper.domain.post.application.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sns.whisper.domain.comment.application.dto.response.CommentServiceResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostServiceResponse {

    private Long id;
    private List<String> imageUrls;
    private String content;
    private String authorUserId;
    private String profileImage;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;
    private List<CommentServiceResponse> comments;

    @Builder
    private PostServiceResponse(Long id, List<String> imageUrls, String content,
            String authorUserId,
            String profileImage, LocalDateTime createdAt, LocalDateTime updatedAt,
            List<CommentServiceResponse> comments) {
        this.id = id;
        this.imageUrls = imageUrls;
        this.content = content;
        this.authorUserId = authorUserId;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
    }
}
