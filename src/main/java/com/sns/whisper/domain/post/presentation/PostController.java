package com.sns.whisper.domain.post.presentation;

import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.application.dto.request.PostDeleteServiceRequest;
import com.sns.whisper.domain.post.presentation.request.PostModifyRequest;
import com.sns.whisper.domain.post.presentation.request.PostUploadRequest;
import com.sns.whisper.global.aop.LoginCheck;
import com.sns.whisper.global.dto.HttpResponseDto;
import com.sns.whisper.global.resolver.AppUser;
import com.sns.whisper.global.resolver.Authenticated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<?> uploadPost(@Valid PostUploadRequest postUploadRequest, @Authenticated
    AppUser appUser) {

        Long postId = postService.uploadPost(
                postUploadRequest.toServiceRequest(appUser.getUserId()));

        return HttpResponseDto.okWithData(HttpStatus.CREATED, "게시물을 업로드했습니다.", postId);
    }

    @PatchMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<?> modifyPost(@PathVariable Long postId,
            @Valid PostModifyRequest postModifyRequest, @Authenticated AppUser appUser) {

        postService.modifyPost(postModifyRequest.toServiceRequest(postId, appUser.getUserId()));

        return HttpResponseDto.ok(HttpStatus.OK, "게시물을 수정했습니다.");
    }

    @DeleteMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @Authenticated AppUser appUser) {

        PostDeleteServiceRequest postDeleteServiceRequest = new PostDeleteServiceRequest(postId,
                appUser.getUserId());

        postService.deletePost(postDeleteServiceRequest);
        return HttpResponseDto.ok(HttpStatus.OK, "게시물을 삭제했습니다.");
    }

}
