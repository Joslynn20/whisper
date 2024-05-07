package com.sns.whisper.common.mockapi;

import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public class MockS3ProfileStorage implements ProfileStorage {

    @Override
    public Optional<String> store(MultipartFile image) {
        return Optional.ofNullable(image.getOriginalFilename());
    }
}
