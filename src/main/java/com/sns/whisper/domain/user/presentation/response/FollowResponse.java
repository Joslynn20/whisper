package com.sns.whisper.domain.user.presentation.response;

import com.sns.whisper.domain.user.application.dto.response.FollowServiceResponse;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class FollowResponse {

    private int followerCount;
    private boolean following;

    public static FollowResponse from(FollowServiceResponse followServiceResponse) {
        return FollowResponse.builder()
                             .followerCount(followServiceResponse.getFollowerCount())
                             .following(followServiceResponse.isFollowing())
                             .build();
    }

}
