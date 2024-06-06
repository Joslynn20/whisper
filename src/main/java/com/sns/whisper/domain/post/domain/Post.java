package com.sns.whisper.domain.post.domain;

import com.sns.whisper.domain.post.domain.content.Images;
import com.sns.whisper.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Embedded
    private Images images;


    protected Post() {
    }

    @Builder
    private Post(Long id, User user, String content, Images images) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.images = images;
    }
}
