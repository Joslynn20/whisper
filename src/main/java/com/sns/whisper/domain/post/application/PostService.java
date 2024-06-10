package com.sns.whisper.domain.post.application;

import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.domain.post.domain.repository.PostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.post.NotFoundUserException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageStorage imageStorage;

    public Long uploadPost(PostUploadServiceRequest serviceRequest) {
        Post post = createPost(serviceRequest);
        return postRepository.save(post)
                             .getId();
    }

    private Post createPost(PostUploadServiceRequest serviceRequest) {
        User user = userRepository.findUserByUserId(serviceRequest.getUserId())
                                  .orElseThrow(NotFoundUserException::new);

        List<String> imageUrls = imageStorage.storeImages(serviceRequest.getImages(),
                serviceRequest.getUserId());

        return Post.builder()
                   .user(user)
                   .content(serviceRequest.getContent())
                   .images(imageUrls)
                   .build();
    }
}
