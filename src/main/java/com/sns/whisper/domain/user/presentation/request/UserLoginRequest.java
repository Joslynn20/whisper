package com.sns.whisper.domain.user.presentation.request;

import static lombok.AccessLevel.PROTECTED;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class UserLoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public UserLoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
