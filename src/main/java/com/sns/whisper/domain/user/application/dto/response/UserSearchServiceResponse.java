package com.sns.whisper.domain.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSearchServiceResponse {


    private String profileImage;
    private String userId;
    private Boolean following;

    @Builder
    public UserSearchServiceResponse(String profileImage, String userId, Boolean following) {
        this.profileImage = profileImage;
        this.userId = userId;
        this.following = following;
    }
}
