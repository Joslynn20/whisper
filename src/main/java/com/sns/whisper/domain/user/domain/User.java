package com.sns.whisper.domain.user.domain;

import com.sns.whisper.domain.post.domain.Posts;
import com.sns.whisper.domain.user.domain.follow.Follow;
import com.sns.whisper.domain.user.domain.follow.Followers;
import com.sns.whisper.domain.user.domain.follow.Followings;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.Email;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import lombok.Builder;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "\"user\"")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BasicProfile basicProfile;

    @Embedded
    private Email email;

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

    @PersistenceCreator
    public User(Long id, BasicProfile basicProfile, Email email, UserStatus status) {
        this(
                id,
                basicProfile,
                email,
                status,
                new Followers(new ArrayList<>()),
                new Followings(new ArrayList<>()),
                new Posts(new ArrayList<>()));
    }


    @Builder
    private User(Long id, BasicProfile basicProfile, Email email, UserStatus status,
            Followers followers,
            Followings followings, Posts posts) {
        this.id = id;
        this.basicProfile = basicProfile;
        this.email = email;
        this.status = status;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
    }

    public static User create(String userId, String password, String email, LocalDate birth,
            String profileImage,
            String profileMessage, LocalDateTime joinedAt) {

        BasicProfile basicProfile = BasicProfile.builder()
                                                .userId(userId)
                                                .password(password)
                                                .birth(birth)
                                                .profileImage(profileImage)
                                                .profileMessage(profileMessage)
                                                .joinedAt(joinedAt)
                                                .build();

        return new User(null, basicProfile, new Email(email), UserStatus.PENDING);
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
        return email.getEmail();
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

    public LocalDateTime getJoinedAt() {
        return basicProfile.getJoinedAt();
    }

    public UserStatus getStatus() {
        return status;
    }


    public Followers getFollowers() {
        return followers;
    }

    public Followings getFollowings() {
        return followings;
    }

    public Posts getPosts() {
        return posts;
    }

    public int getFollowerCount() {
        return followers.count();
    }

    public int getFollowingCount() {
        return followings.count();
    }

    public void follow(User toUser) {
        Follow follow = new Follow(this, toUser);
        this.followings.add(follow);
        toUser.followers.add(follow);
    }


    public Boolean isFollowing(User toUser) {

        if (this.equals(toUser)) {
            return null;
        }

        return this.followings.isFollowing(toUser);
    }

    public void unfollow(User toUser) {
        Follow follow = new Follow(this, toUser);
        this.followings.remove(follow);
        toUser.followers.remove(follow);
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
