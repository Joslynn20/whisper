package com.sns.whisper.domain.user.application.session;

public interface SessionManager {

    void saveUser(String userId);

    String extractUser();

    void deleteUser();
}
