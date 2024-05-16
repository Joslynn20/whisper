package com.sns.whisper.common.mockapi;

import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import java.util.Objects;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

@Profile("test")
public class MockS3ProfileStorage implements ProfileStorage {

    private static final String PROFILE_DIRECTORY = "user/";
    private static final String BASIC_PROFILE = "basic_profile.png";

    @Override
    public Optional<String> store(MultipartFile image, String userId) {

        if (Objects.isNull(image) || image.isEmpty()) {
            return Optional.of(makeBasicProfile());
        }

        return Optional.of(PROFILE_DIRECTORY + image.getOriginalFilename());
    }

    private String makeBasicProfile() {
        return new StringBuilder().append(PROFILE_DIRECTORY)
                                  .append(BASIC_PROFILE)
                                  .toString();
    }
}
