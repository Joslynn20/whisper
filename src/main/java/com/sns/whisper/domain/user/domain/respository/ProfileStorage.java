package com.sns.whisper.domain.user.domain.respository;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileStorage {

    String store(MultipartFile image);
}
