package com.sns.whisper.domain.post.domain.repository;

import com.sns.whisper.domain.post.domain.Post;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

}
