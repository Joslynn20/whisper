package com.sns.whisper.domain.post.presentation;

import com.sns.whisper.domain.post.application.PostFeedService;
import com.sns.whisper.domain.post.application.dto.request.UserFeedServiceRequest;
import com.sns.whisper.domain.post.application.dto.response.PostServiceResponse;
import com.sns.whisper.global.aop.LoginCheck;
import com.sns.whisper.global.dto.HttpResponseDto;
import com.sns.whisper.global.resolver.AppUser;
import com.sns.whisper.global.resolver.Authenticated;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostFeedController {

    private final PostFeedService postFeedService;


    @LoginCheck
    @GetMapping("/my")
    public ResponseEntity<?> readMyFeed(@Authenticated AppUser appUser,
            @PageableDefault Pageable pageable) {

        UserFeedServiceRequest userFeedServiceRequest = new UserFeedServiceRequest(appUser,
                pageable);

        List<PostServiceResponse> postServiceResponses = postFeedService.searchUserFeed(
                userFeedServiceRequest, appUser.getUserId());

        return HttpResponseDto.okWithData(HttpStatus.OK, "내 피드를 조회 성공했습니다.", postServiceResponses);
    }


}
