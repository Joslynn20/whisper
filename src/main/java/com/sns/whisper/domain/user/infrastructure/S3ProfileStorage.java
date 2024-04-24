package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Profile("!test")
@Component
public class S3ProfileStorage implements ProfileStorage {

    private final S3Client s3Client;

    public S3ProfileStorage(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String store(byte[] image) {
        return null;
    }
}
