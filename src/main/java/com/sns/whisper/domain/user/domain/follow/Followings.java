package com.sns.whisper.domain.user.domain.follow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Embeddable
public class Followings {

    @OneToMany(
            mappedBy = "fromUser",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Follow> followings;

    protected Followings() {
    }

    public Followings(List<Follow> followings) {
        this.followings = followings;
    }
}
