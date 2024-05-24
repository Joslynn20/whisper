package com.sns.whisper.global.config;

import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = {JPAUserRepository.class})
@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

}
