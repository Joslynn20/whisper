package com.sns.whisper.domain.post.domain.repository;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

    List<String> storeImages(List<MultipartFile> images, String userId);

    void deleteImages(List<String> imageUrls);
}
