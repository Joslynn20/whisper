package com.sns.whisper.domain.user.presentation.dto.response;

import com.sns.whisper.domain.user.application.dto.response.UserSearchServiceResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSearchResponse {

    private String profileImage;
    private String userId;
    private Boolean following;

    @Builder
    public UserSearchResponse(String profileImage, String userId, Boolean following) {
        this.profileImage = profileImage;
        this.userId = userId;
        this.following = following;
    }

    public static UserSearchResponse from(UserSearchServiceResponse serviceResponse) {
        return UserSearchResponse.builder()
                                 .userId(serviceResponse.getUserId())
                                 .profileImage(serviceResponse.getProfileImage())
                                 .following(serviceResponse.getFollowing())
                                 .build();
    }
}
