package com.sns.whisper.config;

import com.sns.whisper.common.mockapi.MockS3ProfileStorage;
import com.sns.whisper.common.mockapi.MockUserSessionManager;
import com.sns.whisper.domain.user.application.session.SessionManager;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class InfrastructureTestConfiguration {

    @Bean
    public ProfileStorage profileStorage() {
        return new MockS3ProfileStorage();
    }

    @Bean
    public SessionManager sessionManager() {
        return new MockUserSessionManager();
    }
}
