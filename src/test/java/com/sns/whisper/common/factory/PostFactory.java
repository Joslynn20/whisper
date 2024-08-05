package com.sns.whisper.common.factory;

import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.user.domain.User;

public class PostFactory {
    
    public static Post post(User user) {
        return createPost(null, user);
    }

    public static Post post(Long id, User user) {
        return createPost(id, user);
    }

    private static Post createPost(Long id, User user) {
        return MockPost.builder()
                       .id(id)
                       .user(user)
                       .build();
    }

}
