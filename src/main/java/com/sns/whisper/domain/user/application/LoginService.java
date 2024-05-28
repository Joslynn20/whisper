package com.sns.whisper.domain.user.application;

public interface LoginService {

    void login(String userId, String password);

    void logout();
}
