package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.follow.Follow;
import com.sns.whisper.domain.user.domain.follow.Followers;
import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.InvalidFollowException;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class FollowersTest {

    @Test
    @DisplayName("동일한 팔로우를 추가할 경우 예외가 발생한다.")
    void add_DuplicatedFollow_ExceptionThrown() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Follow follow = new Follow(from, to);

        Followers followers = new Followers(new ArrayList<>());
        followers.add(follow);

        Follow sameFollow = new Follow(from, to);

        //when, then
        assertThatCode(() -> followers.add(sameFollow))
                .isInstanceOf(DuplicatedFollowException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("존재하지 않는 팔로우를 삭제할 경우 예외가 발생한다.")
    void remove_InvalidFollow_ExceptionThrown() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followers followers = new Followers(new ArrayList<>());

        Follow invalid = new Follow(from, to);

        //when, then
        assertThatCode(() -> followers.remove(invalid))
                .isInstanceOf(InvalidFollowException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("팔로워들의 수를 반환한다.")
    void size_validFollow_Success() throws Exception {
        //given
        User from = UserFactory.user(1L, "testId");
        User to = UserFactory.user(2L, "testId2");

        Followers followers = new Followers(new ArrayList<>());
        Follow follow = new Follow(from, to);
        
        followers.add(follow);

        //when
        int size = followers.size();

        // then
        assertThat(size).isEqualTo(1);

    }


}
