package com.sns.whisper.domain.user.presentation;

import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.presentation.request.UserSignUpRequest;
import com.sns.whisper.global.dto.HttpResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
