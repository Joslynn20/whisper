package com.sns.whisper.domain.post.domain.content;

import com.sns.whisper.domain.post.domain.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Images {

    @OneToMany(mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Image> images = new ArrayList<>();

    protected Images() {
    }

    public void belongTo(Post post) {
        images.forEach(image -> image.belongTo(post));
    }

    public List<String> getImageUrls() {
        return images.stream()
                     .map(Image::getUrl)
                     .collect(Collectors.toList());
    }

    public Images(List<Image> images) {
        this.images = images;
    }
}
