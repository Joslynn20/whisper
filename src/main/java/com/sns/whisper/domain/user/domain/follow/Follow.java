package com.sns.whisper.domain.user.domain.follow;

import com.sns.whisper.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(
        // 같은 회원을 한 번만 팔로우 가능
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
        }
)
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

    @CreatedDate
    private LocalDateTime followed_at;

    protected Follow() {
    }

    public Follow(Long id, User fromUser, User toUser, LocalDateTime followed_at) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.followed_at = followed_at;
    }

}
