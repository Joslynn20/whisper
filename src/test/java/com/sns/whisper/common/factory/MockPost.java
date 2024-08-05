package com.sns.whisper.common.factory;

import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.content.Image;
import com.sns.whisper.domain.post.domain.content.Images;
import com.sns.whisper.domain.user.domain.User;
import java.util.List;

public class MockPost {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private User user;
        private Images images = new Images(
                List.of(new Image("www.testImage1.png"), new Image("www.testImage2.png")));
        private String content = "게시물 내용입니다.";

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder images(Images images) {
            this.images = images;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }


        public Post build() {
            return Post.builder()
                       .id(id)
                       .user(user)
                       .content(content)
                       .images(images)
                       .build();
        }
    }
}
