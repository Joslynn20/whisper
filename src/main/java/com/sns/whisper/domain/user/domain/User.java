package com.sns.whisper.domain.user.domain;

import com.sns.whisper.domain.post.domain.Posts;
import com.sns.whisper.domain.user.domain.follow.Followers;
import com.sns.whisper.domain.user.domain.follow.Followings;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import lombok.Builder;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Embedded
    private BasicProfile basicProfile;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Embedded
    private Followers followers;

    @Embedded
    private Followings followings;

    @Embedded
    private Posts posts;

    protected User() {
    }

    public User(BasicProfile basicProfile, UserStatus status) {
        this(null, basicProfile, status);
    }

    @PersistenceCreator
    public User(Long id, BasicProfile basicProfile, UserStatus status) {
        this(
                id,
                basicProfile,
                status,
                new Followers(new ArrayList<>()),
                new Followings(new ArrayList<>()),
                new Posts(new ArrayList<>())
        );
    }

    @Builder
    private User(Long id, BasicProfile basicProfile, UserStatus status, Followers followers,
            Followings followings, Posts posts) {
        this.id = id;
        this.basicProfile = basicProfile;
        this.status = status;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return basicProfile.getUserId();
    }

    public String getPassword() {
        return basicProfile.getPassword();
    }

    public String getEmail() {
        return basicProfile.getEmail();
    }

    public LocalDate getBirth() {
        return basicProfile.getBirth();
    }

    public String getProfileImage() {
        return basicProfile.getProfileImage();
    }

    public String getProfileMessage() {
        return basicProfile.getProfileMessage();
    }

    public UserStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
