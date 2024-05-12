package com.sns.whisper.global.common;

import java.util.UUID;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String makeFileName(MultipartFile multipartFile) {
        return createFileName(multipartFile).toString();
    }

    private static StringBuilder createFileName(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID()
                          .toString();

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        StringBuilder fileName = new StringBuilder();

        fileName.append(uuid)
                .append(".")
                .append(extension);

        return fileName;
    }


}
