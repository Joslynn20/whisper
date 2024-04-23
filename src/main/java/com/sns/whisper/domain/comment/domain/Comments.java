package com.sns.whisper.domain.comment.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Comments {

    @OneToMany(
            mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<Comment> comments = new ArrayList<>();

    protected Comments() {
    }

    public Comments(List<Comment> comments) {
        this.comments = comments;
    }
}
