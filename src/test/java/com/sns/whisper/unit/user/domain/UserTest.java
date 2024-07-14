package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.follow.Follow;
import com.sns.whisper.domain.user.domain.follow.Followings;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.NotValidEmailFormatException;
import com.sns.whisper.exception.user.SameFromToUserException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class UserTest {

    @DisplayName("회원 생성 시 처음 회원의 상태는 PENDING(가입 대기)이다.")
    @Test
    void create_ValidUser_Success() {
        // given
        String email = "test@gmail.com";
        // when
        User user = createUser(LocalDateTime.now(), email);
        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @DisplayName("회원 생성 시 가입일자를 기록한다.")
    @Test
    void create_registerDateTime_Success() {
        // given
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@gmail.com";

        // when
        User user = createUser(joinedAt, email);

        // then
        assertThat(user.getJoinedAt()).isEqualTo(joinedAt);
    }


    @Test
    @DisplayName("올바른 형식의 이메일을 입력하면, 회원을 생성할 수 있다")
    void create_ValidEmailFormat_Success() throws Exception {
        // given
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@gmail.com";

        //when, then
        assertThatCode(
                () -> createUser(joinedAt, email)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 형식의 이메일을 입력할 수 없다.")
    void create_inValidEmailFormat_ExceptionThrown() throws Exception {
        // given
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@test";

        //when, then
        assertThatCode(
                () -> createUser(joinedAt, email)).isInstanceOf(NotValidEmailFormatException.class)
                                                  .hasFieldOrPropertyWithValue("httpStatus",
                                                          HttpStatus.BAD_REQUEST)
                                                  .hasMessage("잘못된 형식의 이메일입니다.");
    }

    @Test
    @DisplayName("Gmail 계정이 아닌 이메일을 입력할 수 없다.")
    void create_inValidEmailAccount_ExceptionThrown() throws Exception {
        //given

        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@naver.com";

        //when, then
        assertThatCode(
                () -> createUser(joinedAt, email)).isInstanceOf(NotValidEmailFormatException.class)
                                                  .hasFieldOrPropertyWithValue("httpStatus",
                                                          HttpStatus.BAD_REQUEST)
                                                  .hasMessage("잘못된 형식의 이메일입니다.");
    }

    @DisplayName("Follow 메서드는")
    @Nested
    class Describe_follow {

        @DisplayName("아직 팔로우하지 않은 타 회원에 대해서")
        @Nested
        class Context_ValidOtherUser {

            @Test
            @DisplayName("팔로우에 성공한다.")
            void follow_ValidUser_Success() throws Exception {
                //given
                User from = UserFactory.user(1L, "testId");
                User to = UserFactory.user(2L, "testId2");

                //when
                from.follow(to);

                Field followingsField = User.class.getDeclaredField("followings");
                followingsField.setAccessible(true);
                Followings followings = (Followings) followingsField.get(from);

                //then
                assertThat(followings.contains(new Follow(from, to))).isTrue();
                assertThat(from.getFollowingCount()).isEqualTo(1);
                assertThat(to.getFollowerCount()).isEqualTo(1);
            }
        }

        @DisplayName("이미 팔로우한 타 회원에 대해서")
        @Nested
        class Context_FollowedUser {

            @Test
            @DisplayName("팔로우할 수 없다.")
            void follow_FollowedUser_ExceptionThrown() throws Exception {
                //given
                User from = UserFactory.user(1L, "testId");
                User to = UserFactory.user(2L, "testId2");
                from.follow(to);

                //when, then
                User toUser = UserFactory.user(2L, "testId2");
                assertThatCode(() -> from.follow(toUser)).isInstanceOf(
                                                                 DuplicatedFollowException.class)
                                                         .hasFieldOrPropertyWithValue("httpStatus",
                                                                 HttpStatus.BAD_REQUEST);
            }
        }

        @DisplayName("자기 자신에 대해서")
        @Nested
        class Context_MySelf {

            @Test
            @DisplayName("팔로우할 수 없다.")
            void follow_MySelf_ExceptionThrown() throws Exception {
                //given
                User from = UserFactory.user(1L, "testId");
                User to = UserFactory.user(1L, "testId");

                //when, then
                assertThatCode(() -> from.follow(to)).isInstanceOf(
                                                             SameFromToUserException.class)
                                                     .hasFieldOrPropertyWithValue("httpStatus",
                                                             HttpStatus.BAD_REQUEST);
            }
        }
    }

    private User createUser(LocalDateTime joinedAt, String email) {

        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";

        return User.create(userId, password, email, birth, profileImage, profileMessage,
                joinedAt);
    }

}