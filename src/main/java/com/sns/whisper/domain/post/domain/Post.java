package com.sns.whisper.domain.post.domain;

import static java.util.stream.Collectors.toList;

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

    private Post(Long id, User user, String content, Images images) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public List<String> getImageUrls() {
        return images.getImageUrls();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private User user;
        private Images images = new Images(List.of());
        private String content;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder images(List<String> imageUrls) {
            List<Image> images = imageUrls.stream()
                                          .map(Image::new)
                                          .collect(toList());

            this.images = new Images(images);
            return this;
        }

        public Builder images(Images images) {
            this.images = images;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Post build() {
            return new Post(
                    id,
                    user,
                    content,
                    images
            );
        }
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
