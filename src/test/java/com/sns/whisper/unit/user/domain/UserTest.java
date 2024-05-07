package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.exception.user.NotValidEmailFormatException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class UserTest {

    @DisplayName("회원 생성 시 처음 회원의 상태는 PENDING(가입 대기)이다.")
    @Test
    void create_ValidUser_Success() {
        // given
        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";
        String email = "test@gmail.com";
        // when
        User user = User.create(userId, password, email, birth, profileImage, profileMessage,
                LocalDateTime.now());
        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @DisplayName("회원 생성 시 가입일자를 기록한다.")
    @Test
    void create_registerDateTime_Success() {
        // given
        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@gmail.com";

        // when
        User user = User.create(userId, password, email, birth, profileImage, profileMessage,
                joinedAt);

        // then
        assertThat(user.getJoinedAt()).isEqualTo(joinedAt);
    }


    @Test
    @DisplayName("올바른 형식의 이메일을 입력하면, 회원을 생성할 수 있다")
    void create_ValidEmailFormat_Success() throws Exception {
        // given
        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@gmail.com";

        //when, then
        assertThatCode(
                () -> User.create(userId, password, email, birth, profileImage, profileMessage,
                        joinedAt)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 형식의 이메일을 입력할 수 없다.")
    void create_inValidEmailFormat_ExceptionThrown() throws Exception {
        // given
        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@test";

        //when, then
        assertThatCode(
                () -> User.create(userId, password, email, birth, profileImage, profileMessage,
                        joinedAt)).isInstanceOf(NotValidEmailFormatException.class)
                                  .hasFieldOrPropertyWithValue("httpStatus",
                                          HttpStatus.BAD_REQUEST)
                                  .hasMessage("잘못된 형식의 이메일입니다.");
    }

    @Test
    @DisplayName("Gmail 계정이 아닌 이메일을 입력할 수 없다.")
    void create_inValidEmailAccount_ExceptionThrown() throws Exception {
        //given
        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@naver.com";

        //when, then
        assertThatCode(
                () -> User.create(userId, password, email, birth, profileImage, profileMessage,
                        joinedAt)).isInstanceOf(NotValidEmailFormatException.class)
                                  .hasFieldOrPropertyWithValue("httpStatus",
                                          HttpStatus.BAD_REQUEST)
                                  .hasMessage("잘못된 형식의 이메일입니다.");
    }

    @Test
    @DisplayName("profile 이미지 경로가 없을 경우, default 이미지 경로를 저장한다")
    void create_nullProfileImage_SaveDefaultImage() throws Exception {
        //given
        String userId = "회원아이디";
        String password = "비밀번호1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = null;
        String profileMessage = "프로필 메세지";
        LocalDateTime joinedAt = LocalDateTime.now();
        String email = "test@gmail.com";

        //when
        User newUser = User.create(userId, password, email, birth, profileImage, profileMessage,
                joinedAt);

        // then
        assertThat(newUser.getProfileImage()).isEqualTo("default");
    }
}