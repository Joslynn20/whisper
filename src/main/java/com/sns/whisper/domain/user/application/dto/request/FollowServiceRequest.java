package com.sns.whisper.domain.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FollowServiceRequest {

    private String fromUser;
    private String toUser;

}
