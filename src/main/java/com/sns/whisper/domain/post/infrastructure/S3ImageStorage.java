package com.sns.whisper.domain.post.infrastructure;

import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.exception.user.FileUploadException;
import com.sns.whisper.global.common.FileUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class S3ImageStorage implements ImageStorage {

    private final S3Client s3Client;
    private static final String IMAGE_DIRECTORY = "post";

    @Value("${whisper.aws.s3.bucket}")
    private String bucket;


    @Override
    public List<String> storeImages(List<MultipartFile> images, String userId) {
        return images.stream()
                     .map(image -> uploadImage(
                             FileUtil.makeFileName(IMAGE_DIRECTORY, userId, image),
                             image))
                     .toList();
    }

    private String uploadImage(String key, MultipartFile image) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(key)
                                    .build(),
                    RequestBody.fromByteBuffer(ByteBuffer.wrap(image.getBytes())));

            return s3Client.utilities()
                           .getUrl(GetUrlRequest.builder()
                                                .bucket(bucket)
                                                .key(key)
                                                .build())
                           .toExternalForm();

        } catch (AwsServiceException | SdkClientException | IOException e) {
            throw new FileUploadException();
        }
    }
}
