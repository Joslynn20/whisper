package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@Profile("!test")
@Component
public class S3ProfileStorage implements ProfileStorage {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public S3ProfileStorage(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String store(MultipartFile image) {
        return null;
    }
}
