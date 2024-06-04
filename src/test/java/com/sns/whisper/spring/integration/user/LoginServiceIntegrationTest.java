package com.sns.whisper.spring.integration.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.domain.user.application.session.SessionManager;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.exception.user.LoginFailException;
import com.sns.whisper.global.common.PasswordEncryptor;
import com.sns.whisper.spring.integration.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class LoginServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
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
        loginService.login(userId, password);

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
        assertThatCode(() -> loginService.login(userId, password))
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
        assertThatCode(() -> loginService.login(userId, wrongPassword))
                .isInstanceOf(
                        LoginFailException.class)
                .hasFieldOrPropertyWithValue(
                        "httpStatus",
                        HttpStatus.BAD_REQUEST)
                .hasMessage("아이디와 비밀번호를 확인해주세요.");
    }
}
