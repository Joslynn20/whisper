package com.sns.whisper.spring.integration;

import com.sns.whisper.config.InfrastructureTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Import(InfrastructureTestConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTest {

}
