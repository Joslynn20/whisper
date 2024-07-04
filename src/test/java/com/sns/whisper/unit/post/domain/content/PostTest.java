package com.sns.whisper.unit.post.domain.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.user.domain.User;
import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    @DisplayName("게시물을 작성한 회원과 현재 회원이 같으면, true를 반환한다")
    void isWrittenBy_SameUser_Success() throws Exception {
        //given
        User user = UserFactory.user(1L, "testId");

        Post post = Post.builder()
                        .id(1L)
                        .content("게시물 내용")
                        .user(user)
                        .build();

        //when
        boolean result = post.isWrittenByUser(user);

        //then
        assertThat(result).isTrue();
        Field userField = Post.class.getDeclaredField("user");
        userField.setAccessible(true);
        User fieldUser = (User) userField.get(post);

        assertThat(fieldUser).isEqualTo(user);

    }

    @Test
    @DisplayName("게시물을 작성한 회원과 현재 회원이 다르면, false 값을 반환한다.")
    void isWrittenBy_DifferentUser_Success() throws Exception {
        //given
        User savedUser = UserFactory.user(2L, "differentUser");
        User currentUser = UserFactory.user(1L, "testId");

        Post post = Post.builder()
                        .id(1L)
                        .content("게시물 내용")
                        .user(savedUser)
                        .build();

        //when
        boolean result = post.isWrittenByUser(currentUser);

        //then
        assertThat(result).isFalse();

        Field userField = Post.class.getDeclaredField("user");
        userField.setAccessible(true);
        User fieldUser = (User) userField.get(post);

        assertThat(fieldUser).isNotEqualTo(currentUser);

    }

}
