package com.sns.whisper.domain.post.domain.repository;

import com.sns.whisper.domain.post.domain.Post;

public interface PostRepository {

    Post save(Post post);
    
}
