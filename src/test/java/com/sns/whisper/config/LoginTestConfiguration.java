package com.sns.whisper.config;

import com.sns.whisper.common.mockapi.MockUserSessionManager;
import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.domain.user.application.SessionLoginService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class LoginTestConfiguration {

    @Bean
    public LoginService loginService() {
        return new SessionLoginService(null, new MockUserSessionManager());
    }
}
