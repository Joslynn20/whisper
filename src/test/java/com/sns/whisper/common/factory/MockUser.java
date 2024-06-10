package com.sns.whisper.common.factory;


import com.sns.whisper.domain.post.domain.Posts;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.follow.Followers;
import com.sns.whisper.domain.user.domain.follow.Followings;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.Email;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MockUser {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String userId;
        private String password = "testPassword";
        private LocalDate birth = LocalDate.of(1991, 11, 14);
        private String profileImage = "testImage.png";
        private String profileMessage = "프로필 메세지입니다.";
        private LocalDateTime joinedAt = LocalDateTime.now();
        private String email = "testEmail@gmail.com";
        private UserStatus status = UserStatus.PENDING;
        private Followers followers = new Followers(new ArrayList<>());
        private Followings followings = new Followings(new ArrayList<>());
        private Posts posts = new Posts(new ArrayList<>());

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder birth(LocalDate birth) {
            this.birth = birth;
            return this;
        }

        public Builder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Builder profileMessage(String profileMessage) {
            this.profileMessage = profileMessage;
            return this;
        }

        public Builder joinedAt(LocalDateTime joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public Builder followers(Followers followers) {
            this.followers = followers;
            return this;
        }

        public Builder followings(Followings followings) {
            this.followings = followings;
            return this;
        }

        public Builder posts(Posts posts) {
            this.posts = posts;
            return this;
        }

        public User build() {
            return User.builder()
                       .id(id)
                       .basicProfile(BasicProfile.builder()
                                                 .userId(userId)
                                                 .password(password)
                                                 .birth(birth)
                                                 .profileImage(profileImage)
                                                 .profileMessage(profileMessage)
                                                 .joinedAt(joinedAt)
                                                 .build())
                       .email(new Email(email))
                       .followers(followers)
                       .followings(followings)
                       .posts(posts)
                       .build();
        }

    }

}
