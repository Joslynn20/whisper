package com.sns.whisper.domain.post.infrastructure;

import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JPAPostRepository jpaPostRepository;

    @Override
    public Post save(Post post) {
        return jpaPostRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id);
    }

    @Override
    public void delete(Post post) {
        jpaPostRepository.delete(post);
    }
}
