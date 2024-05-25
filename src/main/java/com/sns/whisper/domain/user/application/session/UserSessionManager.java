package com.sns.whisper.domain.user.application;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class UserSessionManager implements SessionManager {

    private static final String USER_SESSION_KEY = "ACCESS_USER";
    private final HttpSession session;

    @Override
    public void saveUser(String userId) {
        session.setAttribute(USER_SESSION_KEY, userId);
    }

    @Override
    public String extractUser(String userId) {
        return null;
    }
}
