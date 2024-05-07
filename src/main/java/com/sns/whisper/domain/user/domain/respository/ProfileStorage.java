package com.sns.whisper.domain.user.domain.respository;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileStorage {

    Optional<String> store(MultipartFile image);
}
