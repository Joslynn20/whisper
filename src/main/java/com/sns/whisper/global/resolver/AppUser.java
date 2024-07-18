package com.sns.whisper.global.resolver;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AppUser {

    private String userId;

    public String getUserId() {
        return userId;
    }

    abstract public boolean isGuest();
}
