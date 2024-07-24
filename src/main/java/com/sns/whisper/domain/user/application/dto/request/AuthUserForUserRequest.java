package com.sns.whisper.domain.user.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthUserForUserRequest {

    private String userId;
    private boolean guest;

    @Builder
    public AuthUserForUserRequest(String userId, boolean guest) {
        this.userId = userId;
        this.guest = guest;
    }
}
