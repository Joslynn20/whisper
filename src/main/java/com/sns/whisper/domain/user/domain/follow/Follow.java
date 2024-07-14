package com.sns.whisper.domain.user.domain.follow;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.exception.user.SameFromToUserException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.Getter;

@Entity
@Table(
        // 같은 회원을 한 번만 팔로우 가능
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
        }
)
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우를 하는 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    // 팔로우를 받는 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;


    protected Follow() {
    }

    public Follow(User fromUser, User toUser) {
        validateDifferentUsers(fromUser, toUser);
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    private void validateDifferentUsers(User fromUser, User toUser) {
        if (fromUser.equals(toUser)) {
            throw new SameFromToUserException();
        }
    }

    public boolean isFollowing(User toUser) {
        return this.toUser.equals(toUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Follow follow = (Follow) o;
        return Objects.equals(fromUser, follow.getFromUser()) && Objects.equals(toUser,
                follow.getToUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUser, toUser);
    }
}
