package com.sns.whisper.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
@Profile("!test")
@PropertySource("classpath:/environment.properties")
public class S3Config {

    @Value("${aws.iam.accessKey}")
    private String accessKey;

    @Value("${aws.iam.secretKey}")
    private String accessSecret;

    @Value("${whisper.aws.s3.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                       .credentialsProvider(StaticCredentialsProvider.create(
                               AwsBasicCredentials.create(accessKey, accessSecret)))
                       .region(Region.of(region))
                       .build();
    }

}