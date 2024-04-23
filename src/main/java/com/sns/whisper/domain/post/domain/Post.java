package com.sns.whisper.domain.post.domain;

import com.sns.whisper.domain.post.domain.content.Images;
import com.sns.whisper.domain.post.domain.content.PostContent;
import com.sns.whisper.domain.user.domain.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private PostContent content;

    @Embedded
    private Images images;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    protected Post() {
    }

    public Post(Long id, User user, PostContent content, Images images, LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
