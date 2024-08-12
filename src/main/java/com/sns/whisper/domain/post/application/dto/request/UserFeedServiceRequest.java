package com.sns.whisper.domain.post.application.dto.request;

import com.sns.whisper.global.resolver.AppUser;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class UserFeedServiceRequest {

    private String requestUserId;
    private boolean guest;
    private Pageable pageable;

    public UserFeedServiceRequest(AppUser appUser, Pageable pageable) {
        if (!appUser.isGuest()) {
            this.requestUserId = appUser.getUserId();
        }
        this.guest = appUser.isGuest();
        this.pageable = pageable;
    }


}
