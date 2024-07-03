package com.sns.whisper.domain.post.presentation.request;

import com.sns.whisper.domain.post.application.dto.request.PostModifyServiceRequest;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostModifyRequest {


    @Size(max = 500, message = "내용은 500자 이하로 작성해야 합니다.")
    private String content;

    public PostModifyServiceRequest toServiceRequest(Long postId, String userId) {
        return PostModifyServiceRequest.builder()
                                       .postId(postId)
                                       .content(content)
                                       .userId(userId)
                                       .build();
    }

}
