package com.sns.whisper.domain.post.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Embeddable
public class Posts {

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Post> posts;

    protected Posts() {
    }

    public Posts(List<Post> posts) {
        this.posts = posts;
    }
}
