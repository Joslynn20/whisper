package com.sns.whisper.domain.user.presentation;

import com.sns.whisper.domain.user.application.GeneralUserService;
import com.sns.whisper.domain.user.presentation.request.UserSignUpRequest;
import com.sns.whisper.global.dto.HttpResponseDto;
import jakarta.validation.Valid;
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

    private final GeneralUserService userService;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid UserSignUpRequest request) {
        
        LocalDateTime joinedAt = LocalDateTime.now();
        userService.signUp(request.toServiceRequest(joinedAt));

        return HttpResponseDto.ok(HttpStatus.CREATED, "회원가입에 성공했습니다.");
    }

}