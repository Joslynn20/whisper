package com.sns.whisper.global.common;

import java.util.UUID;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String makeFileName(String directory, String userId,
            MultipartFile multipartFile) {
        return createFileName(directory, userId, multipartFile).toString();
    }

    private static StringBuilder createFileName(String directory, String userId,
            MultipartFile multipartFile) {
        String uuid = UUID.randomUUID()
                          .toString();

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        StringBuilder fileName = new StringBuilder();

        fileName.append(directory)
                .append("/")
                .append(userId)
                .append("/")
                .append(uuid)
                .append(".")
                .append(extension);

        return fileName;
    }


}
