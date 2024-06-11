package com.sns.whisper.domain.post.infrastructure;

import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.exception.post.FileDeleteException;
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
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class S3ImageStorage implements ImageStorage {

    private final S3Client s3Client;
    private static final String IMAGE_DIRECTORY = "post";

    @Value("${whisper.aws.s3.bucket}")
    private String bucket;

    @Value("${whisper.aws.s3.base-url}")
    private String baseUrl;


    @Override
    public List<String> storeImages(List<MultipartFile> images, String userId) {
        return images.stream()
                     .map(image -> uploadImage(
                             FileUtil.makeFileName(IMAGE_DIRECTORY, userId, image),
                             image))
                     .toList();
    }

    @Override
    public void deleteImages(List<String> imageUrls) {

        List<ObjectIdentifier> deleteObjects = imageUrls.stream()
                                                        .map(url -> ObjectIdentifier.builder()
                                                                                    .key(url.substring(
                                                                                            baseUrl.length()))
                                                                                    .build())
                                                        .toList();

        sendDeleteRequests(deleteObjects);
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

    private void sendDeleteRequests(List<ObjectIdentifier> deleteObjects) {

        try {
            DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                                                               .bucket(bucket)
                                                               .delete(Delete.builder()
                                                                             .objects(deleteObjects)
                                                                             .build())
                                                               .build();
            s3Client.deleteObjects(request);
        } catch (S3Exception e) {
            throw new FileDeleteException();
        }
    }

}
