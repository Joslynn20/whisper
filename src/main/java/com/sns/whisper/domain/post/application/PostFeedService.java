package com.sns.whisper.domain.post.application;

import com.sns.whisper.domain.post.application.dto.request.UserFeedServiceRequest;
import com.sns.whisper.domain.post.application.dto.response.PostServiceResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostFeedService {

    public List<PostServiceResponse> searchUserFeed(UserFeedServiceRequest userFeedServiceRequest,
            String targetUserId) {

        return null;
    }
}
