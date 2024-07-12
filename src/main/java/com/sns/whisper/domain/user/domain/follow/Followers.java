package com.sns.whisper.domain.user.domain.follow;

import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.InvalidFollowException;
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

    public void add(Follow follow) {
        if (this.followers.contains(follow)) {
            throw new DuplicatedFollowException();
        }
        followers.add(follow);
    }

    public void remove(Follow follow) {
        if (!this.followers.contains(follow)) {
            throw new InvalidFollowException();
        }
        followers.remove(follow);
    }

    public int count() {
        return followers.size();
    }
}
