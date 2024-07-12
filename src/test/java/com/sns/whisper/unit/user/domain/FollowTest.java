package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.follow.Follow;
import com.sns.whisper.exception.user.SameFromToUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class FollowTest {

    @Test
    @DisplayName("회원은 자기 자신을 팔로우할 수 없다.")
    void create_SameFromToUser_ExceptionThrown() throws Exception {
        //given
        User fromUser = UserFactory.user(1L, "testId");
        User toUser = UserFactory.user(1L, "testId");

        //when, then
        assertThatCode(() -> new Follow(fromUser, toUser))
                .isInstanceOf(SameFromToUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("팔로잉하고 있는 회원이다.")
    void isFollowing_FollowedUser_True() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");
        User toUser = UserFactory.user(2L, "testId2");

        Follow follow = new Follow(from, to);

        //when, then
        assertThat(follow.isFollowing(toUser)).isTrue();
    }

    @Test
    @DisplayName("팔로잉하고 있는 회원이 아니다.")
    void isFollowing_FollowedUser_False() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");
        User toUser = UserFactory.user(3L, "testId3");

        Follow follow = new Follow(from, to);

        //when, then
        assertThat(follow.isFollowing(toUser)).isFalse();
    }
}
