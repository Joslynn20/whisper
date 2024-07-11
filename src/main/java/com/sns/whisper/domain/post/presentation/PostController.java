package com.sns.whisper.domain.post.presentation;

import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.presentation.request.PostModifyRequest;
import com.sns.whisper.domain.post.presentation.request.PostUploadRequest;
import com.sns.whisper.global.aop.LoginCheck;
import com.sns.whisper.global.dto.HttpResponseDto;
import com.sns.whisper.global.resolver.AuthUser;
import com.sns.whisper.global.resolver.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @LoginCheck
    public ResponseEntity<?> uploadPost(@Valid PostUploadRequest postUploadRequest, @CurrentUser
    AuthUser authUser) {

        Long postId = postService.uploadPost(
                postUploadRequest.toServiceRequest(authUser.getUserId()));

        return HttpResponseDto.okWithData(HttpStatus.CREATED, "게시물을 업로드했습니다.", postId);
    }

    @PatchMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<?> modifyPost(@PathVariable Long postId,
            @Valid PostModifyRequest postModifyRequest, @CurrentUser AuthUser authUser) {

        postService.modifyPost(postModifyRequest.toServiceRequest(postId, authUser.getUserId()));

        return HttpResponseDto.ok(HttpStatus.OK, "게시물을 수정했습니다.");
    }

}
