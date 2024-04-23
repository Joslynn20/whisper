package com.sns.whisper.domain.post.domain.content;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Images {

    @OneToMany(mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Image> images = new ArrayList<>();

    protected Images() {
    }

    public Images(List<Image> images) {
        this.images = images;
    }
}
