package com.sns.whisper.domain.user.presentation;

import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.application.dto.request.AuthUserForUserRequest;
import com.sns.whisper.domain.user.application.dto.request.FollowServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.UserSearchServiceResponse;
import com.sns.whisper.domain.user.presentation.dto.UserAssembler;
import com.sns.whisper.domain.user.presentation.dto.request.UserLoginRequest;
import com.sns.whisper.domain.user.presentation.dto.request.UserSignUpRequest;
import com.sns.whisper.domain.user.presentation.dto.response.FollowResponse;
import com.sns.whisper.domain.user.presentation.dto.response.UserSearchResponse;
import com.sns.whisper.global.aop.LoginCheck;
import com.sns.whisper.global.dto.HttpResponseDto;
import com.sns.whisper.global.resolver.AppUser;
import com.sns.whisper.global.resolver.Authenticated;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> login(@Valid UserLoginRequest request) {
        loginService.login(request.getUserId(), request.getPassword());
        return HttpResponseDto.ok(HttpStatus.OK, "로그인되었습니다.");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout() {
        loginService.logout();
        return HttpResponseDto.ok(HttpStatus.OK, "로그아웃되었습니다.");
    }

    @LoginCheck
    @PostMapping("/{userId}/followings")
    public ResponseEntity<?> followUser(@Authenticated AppUser appUser,
            @PathVariable String userId) {

        FollowServiceRequest serviceRequest = new FollowServiceRequest(appUser.getUserId(),
                userId);

        FollowResponse followResponse = FollowResponse.from(userService.followUser(serviceRequest));

        return HttpResponseDto.okWithData(HttpStatus.CREATED, userId + "님을 팔로우했습니다.",
                followResponse);
    }

    @LoginCheck
    @DeleteMapping("/{userId}/followings")
    public ResponseEntity<?> unfollowUser(@Authenticated AppUser appUser,
            @PathVariable String userId) {

        FollowServiceRequest serviceRequest = new FollowServiceRequest(appUser.getUserId(),
                userId);

        FollowResponse followResponse = FollowResponse.from(
                userService.unfollowUser(serviceRequest));

        return HttpResponseDto.okWithData(HttpStatus.OK, userId + "님을 언팔로우했습니다.",
                followResponse);
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<?> searchFollowings(@PageableDefault Pageable pageable,
            @PathVariable(name = "userId") String fromUser, @Authenticated AppUser appUser) {

        AuthUserForUserRequest authUser = UserAssembler.getAuthUser(appUser);

        List<UserSearchServiceResponse> serviceResponse =
                userService.searchFollowings(pageable, fromUser, authUser);

        List<UserSearchResponse> response = serviceResponse.stream()
                                                           .map(UserSearchResponse::from)
                                                           .collect(Collectors.toList());

        return HttpResponseDto.okWithData(HttpStatus.OK, "팔로잉 목록을 조회했습니다.",
                response);
    }
}
