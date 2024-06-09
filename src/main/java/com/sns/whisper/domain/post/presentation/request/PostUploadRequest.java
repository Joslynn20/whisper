package com.sns.whisper.domain.post.presentation.request;

import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.global.validation.AllowedContentType;
import com.sns.whisper.global.validation.MaxImageAmount;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUploadRequest {

    @Size(max = 500, message = "내용은 500자 이하로 작성해야 합니다.")
    private String content;

    @MaxImageAmount
    private List<@AllowedContentType(allowedTypes = {"image/jpg", "image/jpeg", "image/png"},
            allowedExtensions = {"jpg", "jpeg", "png"}) MultipartFile> images = new ArrayList<>();

    public PostUploadServiceRequest toServiceRequest(String userId) {
        return PostUploadServiceRequest.builder()
                                       .content(content)
                                       .userId(userId)
                                       .images(images)
                                       .build();
    }
}
