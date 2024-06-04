package com.sns.whisper.common.mockapi;

import com.sns.whisper.domain.user.application.session.SessionManager;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpSession;

@Profile("test")
public class MockUserSessionManager implements SessionManager {

    private static final String USER_SESSION_KEY = "ACCESS_USER";
    private final MockHttpSession session;

    public MockUserSessionManager() {
        this.session = new MockHttpSession();
    }

    @Override
    public void saveUser(String userId) {
        session.setAttribute(USER_SESSION_KEY, userId);
    }

    @Override
    public String extractUser() {
        return (String) session.getAttribute(USER_SESSION_KEY);
    }

    @Override
    public void deleteUser() {
        session.removeAttribute(USER_SESSION_KEY);
    }
}
