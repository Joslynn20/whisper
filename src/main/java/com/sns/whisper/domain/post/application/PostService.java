package com.sns.whisper.domain.post.application;

import com.sns.whisper.domain.post.application.dto.request.PostModifyServiceRequest;
import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.domain.post.domain.repository.PostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.event.post.UploadRollbackEvent;
import com.sns.whisper.exception.post.NotFoundPostException;
import com.sns.whisper.exception.post.NotFoundUserException;
import com.sns.whisper.exception.post.PostNotBelongToUserException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageStorage imageStorage;
    private final ApplicationEventPublisher eventPublisher;


    public Long uploadPost(PostUploadServiceRequest serviceRequest) {
        Post post = createPost(serviceRequest);

        eventPublisher.publishEvent(new UploadRollbackEvent(post));

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

    public void modifyPost(PostModifyServiceRequest serviceRequest) {
        User user = userRepository.findUserByUserId(serviceRequest.getUserId())
                                  .orElseThrow(NotFoundUserException::new);

        Post post = postRepository.findById(serviceRequest.getPostId())
                                  .orElseThrow(NotFoundPostException::new);

        if (!post.isWrittenByUser(user)) {
            throw new PostNotBelongToUserException();
        }
        
        post.updateContent(serviceRequest.getContent());

    }
}
