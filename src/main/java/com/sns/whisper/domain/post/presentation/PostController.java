package com.sns.whisper.domain.post.presentation;

import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.presentation.request.PostUploadRequest;
import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.exception.post.NotAuthorizedUserException;
import com.sns.whisper.global.dto.HttpResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<?> uploadPost(@Valid PostUploadRequest postUploadRequest) {

        String userId = loginService.getCurrentUserId();
        if (userId == null) {
            throw new NotAuthorizedUserException();
        }
        
        Long postId = postService.uploadPost(postUploadRequest.toServiceRequest(userId));

        return HttpResponseDto.okWithData(HttpStatus.CREATED, "게시물을 업로드했습니다.", postId);
    }

}
