package com.sns.whisper.domain.post.domain;

import com.sns.whisper.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    protected Post() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "post_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


}
