package com.sns.whisper.domain.user.domain.follow;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.InvalidFollowException;
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

    public void add(Follow follow) {
        if (this.followings.contains(follow)) {
            throw new DuplicatedFollowException();
        }
        followings.add(follow);
    }

    public void remove(Follow follow) {
        if (!this.followings.contains(follow)) {
            throw new InvalidFollowException();
        }
        followings.remove(follow);
    }

    public boolean isFollowing(User toUser) {
        return followings.stream()
                         .anyMatch(follow -> follow.isFollowing(toUser));
    }

    public boolean contains(Follow follow) {
        return this.followings.contains(follow);
    }

    public int size() {
        return followings.size();
    }
}
