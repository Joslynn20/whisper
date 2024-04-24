package com.sns.whisper.domain.user.domain.profile;

public enum UserStatus {
    DELETED("탈퇴한 회원"),
    REGISTERED("가입한 회원");

    private final String text;

    UserStatus(String text) {
        this.text = text;
    }
}
