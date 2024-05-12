package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.exception.user.FileUploadException;
import com.sns.whisper.global.common.FileUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class S3ProfileStorage implements ProfileStorage {

    private final S3Client s3Client;

    private static final String DIRECTORY = "user/";

    @Value("${whisper.aws.s3.bucket}")
    private String bucket;

    @Value("${whisper.aws.s3.base-url}")
    private String baseUrl;


    @Override
    public Optional<String> store(MultipartFile image, String userId) {

        if (Objects.isNull(image) || image.isEmpty()) {
            return Optional.ofNullable(null);
        }

        String newFileName = FileUtil.makeFileName(image);

        StringBuilder key = new StringBuilder();

        key.append(DIRECTORY)
           .append("/")
           .append(userId)
           .append("/")
           .append(newFileName);

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(String.valueOf(key))
                                    .build(),
                    RequestBody.fromByteBuffer(ByteBuffer.wrap(image.getBytes())));

            String url = s3Client.utilities()
                                 .getUrl(GetUrlRequest.builder()
                                                      .bucket(bucket)
                                                      .key(String.valueOf(key))
                                                      .build())
                                 .toExternalForm();

            return Optional.ofNullable(url);

        } catch (IOException e) {
            throw new FileUploadException();
        }
    }

}
