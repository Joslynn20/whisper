package com.sns.whisper.domain.user.presentation.dto;

import com.sns.whisper.domain.user.application.dto.request.AuthUserForUserRequest;
import com.sns.whisper.global.resolver.AppUser;

public class UserAssembler {

    public static AuthUserForUserRequest getAuthUser(AppUser appUser) {
        if (appUser.isGuest()) {
            return AuthUserForUserRequest.builder()
                                         .guest(true)
                                         .build();
        }

        return AuthUserForUserRequest.builder().
                                     userId(appUser.getUserId()).
                                     build();
    }

}
