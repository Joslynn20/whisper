package com.sns.whisper.common.mockapi;

import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.global.common.FileUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

@Profile("test")
public class MockS3ImageStorage implements ImageStorage {

    private static final String IMAGE_DIRECTORY = "post";

    @Override
    public List<String> storeImages(List<MultipartFile> images, String userId) {
        return images.stream()
                     .map(image -> FileUtil.makeFileName(IMAGE_DIRECTORY, userId, image))
                     .collect(Collectors.toList());
    }

    @Override
    public void deleteImages(List<String> imageUrls) {
        
    }
}
