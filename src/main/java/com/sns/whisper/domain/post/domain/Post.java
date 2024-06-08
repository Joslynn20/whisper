package com.sns.whisper.domain.post.domain;

import com.sns.whisper.domain.post.domain.content.Image;
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
import java.util.List;
import java.util.Objects;
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
    private Post(Long id, User user, String content, List<String> imageUrls) {
        this.id = id;
        this.user = user;
        this.content = content;
        List<Image> images = imageUrls.stream()
                                      .map(Image::new)
                                      .toList();
        this.images = new Images(images);
        this.images.belongTo(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
