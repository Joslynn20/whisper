package com.sns.whisper.domain.user.domain.follow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Embeddable
public class Followers {

    @OneToMany(
            mappedBy = "toUser",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Follow> followers;

    protected Followers() {
    }

    public Followers(List<Follow> followers) {
        this.followers = followers;
    }
}
