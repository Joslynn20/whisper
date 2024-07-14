package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.follow.Follow;
import com.sns.whisper.domain.user.domain.follow.Followings;
import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.InvalidFollowException;
import java.util.ArrayList;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class FollowingsTest {

    @Test
    @DisplayName("동일한 팔로우를 추가할 경우 예외가 발생한다.")
    void add_DuplicatedFollow_ExceptionThrown() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Follow follow = new Follow(from, to);

        Followings followings = new Followings(new ArrayList<>());
        followings.add(follow);

        //when, then
        Follow sameFollow = new Follow(from, to);

        assertThat(sameFollow).isEqualTo(follow);
        assertThatCode(() -> followings.add(sameFollow))
                .isInstanceOf(DuplicatedFollowException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("존재하지 않는 팔로우를 삭제할 경우 예외가 발생한다.")
    void remove_InvalidFollow_ExceptionThrown() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followings followings = new Followings(new ArrayList<>());

        //when, then
        Follow invalid = new Follow(from, to);
        assertThatCode(() -> followings.remove(invalid))
                .isInstanceOf(InvalidFollowException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("팔로잉하고 있는 회원이다.")
    void isFollowing_FollowedUser_True() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followings followings = new Followings(new ArrayList<>());
        followings.add(new Follow(from, to));

        //when
        boolean result = followings.isFollowing(to);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("팔로잉하지 않은 회원이다.")
    void isFollowing_NotFollowedUser_False() throws Exception {
        //given
        User to = UserFactory.user(2L, "testId2");

        Followings followings = new Followings(new ArrayList<>());

        //when
        boolean result = followings.isFollowing(to);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("팔로잉 리스트에 팔로우가 포함되어 있다.")
    void contains_Follow_True() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followings followings = new Followings(new ArrayList<>());
        Follow follow = new Follow(from, to);

        followings.add(follow);

        //when
        Follow sameFollow = new Follow(from, to);
        boolean result = followings.contains(sameFollow);

        //then
        assertThat(follow).isEqualTo(sameFollow);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("팔로잉 리스트에 팔로우가 포함되어 있지 않다.")
    void contains_InvalidFollow_False() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followings followings = new Followings(new ArrayList<>());

        //when
        boolean result = followings.contains(new Follow(from, to));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("팔로잉 수를 반환한다.")
    void size_validFollow_Success() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followings followings = new Followings(new ArrayList<>());
        Follow follow = new Follow(from, to);

        followings.add(follow);

        //when
        int size = followings.count();

        // then
        AssertionsForClassTypes.assertThat(size)
                               .isEqualTo(1);

    }

}
