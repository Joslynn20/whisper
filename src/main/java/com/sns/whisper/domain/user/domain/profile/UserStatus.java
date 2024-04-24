package com.sns.whisper.domain.user.domain.profile;

public enum UserStatus {
    DELETED("탈퇴"),
    PENDING("가입 대기"),
    APPROVED("가입");

    private final String text;

    UserStatus(String text) {
        this.text = text;
    }
}
