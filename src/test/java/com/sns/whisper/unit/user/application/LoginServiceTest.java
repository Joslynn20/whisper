package com.sns.whisper.unit.user.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.common.mockapi.MockUserSessionManager;
import com.sns.whisper.domain.user.application.SessionLoginService;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.user.LoginFailException;
import com.sns.whisper.global.common.PasswordEncryptor;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private SessionLoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private MockUserSessionManager sessionManager;


    @Test
    @DisplayName("가입회원의 유효한 아이디와 비밀번호로 로그인할 수 있다.")
    void login_ValidUser_Success() throws Exception {
        //given
        String userId = "userId123";
        String password = "password1234";

        User savedUser = UserFactory.createBasicUser(userId, password);
        when(userRepository.findUserByUserId(userId)).thenReturn(Optional.of(savedUser));

        //when
        loginService.login(userId, password);

        //then
        assertThat(sessionManager.extractUser("ACCESS_USER")).isEqualTo(userId);
        assertThat(PasswordEncryptor.isMatch(password, savedUser.getPassword())).isTrue();
        verify(userRepository).findUserByUserId(userId);
    }

    @Test
    @DisplayName("가입하지 않은 회원은 로그인을 할 수 없다.")
    void login_NotValidUserId_ExceptionThrown() throws Exception {
        //given
        String userId = "userId123";
        String password = "password1234";

        //when
        assertThatCode(() -> loginService.login(userId, password))
                .isInstanceOf(
                        LoginFailException.class)
                .hasFieldOrPropertyWithValue(
                        "httpStatus",
                        HttpStatus.BAD_REQUEST)
                .hasMessage("아이디와 비밀번호를 확인해주세요.");

        verify(userRepository, times(1)).findUserByUserId(any(String.class));
        verify(sessionManager, never()).saveUser(any(String.class));
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호를 입력할 경우 로그인을 할 수 없다.")
    void login_NotValidPassword_ExceptionThrown() throws Exception {
        //given
        String userId = "userId123";
        String password = "password1234";

        String savedPassword = "testPassword1234";

        User savedUser = UserFactory.createBasicUser(userId, savedPassword);

        when(userRepository.findUserByUserId(userId)).thenReturn(Optional.of(savedUser));

        //when
        assertThatCode(() -> loginService.login(userId, password))
                .isInstanceOf(
                        LoginFailException.class)
                .hasFieldOrPropertyWithValue(
                        "httpStatus",
                        HttpStatus.BAD_REQUEST)
                .hasMessage("아이디와 비밀번호를 확인해주세요.");

        verify(userRepository, times(1)).findUserByUserId(any(String.class));
        verify(sessionManager, never()).saveUser(any(String.class));
    }
}
