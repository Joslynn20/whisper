package com.sns.whisper.domain.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowServiceResponse {

    private int followerCount;
    private boolean following;

    @Builder
    public FollowServiceResponse(int followerCount, boolean following) {
        this.followerCount = followerCount;
        this.following = following;
    }
}
