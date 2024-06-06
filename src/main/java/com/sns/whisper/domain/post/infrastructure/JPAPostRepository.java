package com.sns.whisper.domain.post.infrastructure;

import com.sns.whisper.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JPAPostRepository extends JpaRepository<Post, Long> {


}
