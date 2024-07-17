package com.sns.whisper.domain.user.presentation;

import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.application.dto.request.FollowServiceRequest;
import com.sns.whisper.domain.user.presentation.request.UserSignUpRequest;
import com.sns.whisper.domain.user.presentation.response.FollowResponse;
import com.sns.whisper.global.aop.LoginCheck;
import com.sns.whisper.global.dto.HttpResponseDto;
import com.sns.whisper.global.resolver.AuthUser;
import com.sns.whisper.global.resolver.Authenticated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid UserSignUpRequest request) {

        LocalDateTime joinedAt = LocalDateTime.now();
        userService.signUp(request.toServiceRequest(joinedAt));

        return HttpResponseDto.ok(HttpStatus.CREATED, "회원가입에 성공했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@NotBlank String userId, @NotBlank String password) {
        loginService.login(userId, password);
        return HttpResponseDto.ok(HttpStatus.OK, "로그인되었습니다.");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout() {
        loginService.logout();
        return HttpResponseDto.ok(HttpStatus.OK, "로그아웃되었습니다.");
    }

    @LoginCheck
    @PostMapping("/{userId}/followings")
    public ResponseEntity<?> followUser(@Authenticated AuthUser authUser,
            @PathVariable String userId) {

        FollowServiceRequest serviceRequest = new FollowServiceRequest(authUser.getUserId(),
                userId);

        FollowResponse followResponse = FollowResponse.from(userService.followUser(serviceRequest));

        return HttpResponseDto.okWithData(HttpStatus.CREATED, userId + "님을 팔로우했습니다.",
                followResponse);
    }

    @LoginCheck
    @DeleteMapping("/{userId}/followings")
    public ResponseEntity<?> unfollowUser(@Authenticated AuthUser authUser,
            @PathVariable String userId) {

        FollowServiceRequest serviceRequest = new FollowServiceRequest(authUser.getUserId(),
                userId);

        FollowResponse followResponse = FollowResponse.from(
                userService.unfollowUser(serviceRequest));

        return HttpResponseDto.okWithData(HttpStatus.OK, userId + "님을 언팔로우했습니다.",
                followResponse);
    }
}
