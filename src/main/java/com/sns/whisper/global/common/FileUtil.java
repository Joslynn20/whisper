package com.sns.whisper.global.common;

import java.util.UUID;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String makeFileName(String directory, MultipartFile multipartFile) {
        return createFileName(directory, multipartFile).toString();
    }

    private static StringBuilder createFileName(String directory, MultipartFile multipartFile) {
        String uuid = UUID.randomUUID()
                          .toString();

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        StringBuilder fileName = new StringBuilder();

        fileName.append(directory)
                .append(uuid)
                .append(".")
                .append(extension);

        return fileName;
    }


}
