package com.sns.whisper.spring.integration.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.application.GeneralUserService;
import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.application.session.SessionManager;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.exception.user.LoginFailException;
import com.sns.whisper.global.common.PasswordEncryptor;
import com.sns.whisper.spring.integration.IntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


public class GeneralUserServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private GeneralUserService userService;

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("회원가입에 성공하면, 회원정보를 반환한다.")
    void signUp_ValidUser_Success() throws Exception {
        //given
        LocalDateTime joinedAt = LocalDateTime.now();
        UserSignUpServiceRequest request = UserSignUpServiceRequest.builder()
                                                                   .userId("user1234")
                                                                   .password("userPassword123")
                                                                   .email("test@gmail.com")
                                                                   .birth(LocalDate.of(1996, 4, 24))
                                                                   .profileImage(null)
                                                                   .profileMessage("회원 메세지")
                                                                   .joinedAt(joinedAt)
                                                                   .build();

        //when
        UserResponse userResponse = userService.signUp(request);

        //then
        assertThat(userResponse).extracting("id", "status", "joinedAt")
                                .contains(1L, UserStatus.PENDING, joinedAt);

        assertThat(userResponse.getProfileImage()).contains("basic_profile.png");
    }

    @Test
    @DisplayName("중복된 아이디는 회원 가입에 실패한다.")
    void signUp_DuplicatedUserId_ThrownException() throws Exception {
        //given
        String userId = "testId1234";

        User formerUser = User.create(userId, "password1234", "user@gmail.com",
                LocalDate.of(1996, 4, 24), "basic_profile.png", "프로필 메세지", LocalDateTime.now());

        userRepository.save(formerUser);

        UserSignUpServiceRequest request = UserSignUpServiceRequest.builder()
                                                                   .userId(userId)
                                                                   .password("userPassword123")
                                                                   .email("test@gmail.com")
                                                                   .birth(LocalDate.of(1996, 4, 24))
                                                                   .profileImage(null)
                                                                   .profileMessage("회원 메세지")
                                                                   .joinedAt(LocalDateTime.now())
                                                                   .build();
        //when, then
        assertThatCode(() -> userService.signUp(request)).isInstanceOf(
                                                                 DuplicatedUserIdException.class)
                                                         .hasFieldOrPropertyWithValue("httpStatus",
                                                                 HttpStatus.BAD_REQUEST)
                                                         .hasMessage("중복된 아이디입니다.");
    }

    @Test
    @DisplayName("가입 회원의 유효한 회원 아이디와 비밀번호를 입력하면, 로그인할 수 있다.")
    void login_ValidUser_Success() throws Exception {
        //given
        String userId = "userId12";
        String password = "password1234";

        User user = UserFactory.createBasicUser(userId, password);
        userRepository.save(user);

        //when
        userService.login(userId, password);

        //then
        assertThat(sessionManager.extractUser("ACCESS_USER")).isEqualTo(userId);
        assertThat(PasswordEncryptor.isMatch(password, user.getPassword())).isTrue();

    }

    @Test
    @DisplayName("가입하지 않은 회원은 로그인을 할 수 없다.")
    void login_NotValidUserId_ExceptionThrown() throws Exception {
        //given
        String userId = "userId123";
        String password = "password1234";

        //when, then
        assertThatCode(() -> userService.login(userId, password))
                .isInstanceOf(
                        LoginFailException.class)
                .hasFieldOrPropertyWithValue(
                        "httpStatus",
                        HttpStatus.BAD_REQUEST)
                .hasMessage("아이디와 비밀번호를 확인해주세요.");

    }

    @Test
    @DisplayName("유효하지 않은 비밀번호를 입력할 경우 로그인을 할 수 없다.")
    void login_NotValidPassword_ExceptionThrown() throws Exception {
        //given
        String userId = "userId123";
        String wrongPassword = "wrongPass1234";

        User user = UserFactory.createBasicUser(userId, "password1234");
        userRepository.save(user);

        //when, then
        assertThatCode(() -> userService.login(userId, wrongPassword))
                .isInstanceOf(
                        LoginFailException.class)
                .hasFieldOrPropertyWithValue(
                        "httpStatus",
                        HttpStatus.BAD_REQUEST)
                .hasMessage("아이디와 비밀번호를 확인해주세요.");
    }

}
