package com.sns.whisper.unit.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.common.mockapi.MockUserSessionManager;
import com.sns.whisper.domain.user.application.GeneralUserService;
import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.Email;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.exception.user.LoginFailException;
import com.sns.whisper.exception.user.NotValidEmailFormatException;
import com.sns.whisper.global.common.PasswordEncryptor;
import java.time.LocalDate;
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
public class UserServiceTest {

    @InjectMocks
    private GeneralUserService userService;

    @Mock
    private ProfileStorage profileStorage;

    @Mock
    private UserRepository userRepository;

    @Spy
    private MockUserSessionManager sessionManager;


    @DisplayName("회원가입 요청을 받으면, 승인 대기 상태의 회원을 생성한다.")
    @Test
    void signUp_ValidUser_ExpectPendingUser() {
        // given
        UserSignUpServiceRequest request = createSignUpRequest("email@gmail.com");

        User savedUser = createUser(request);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(profileStorage.store(any(), any())).thenReturn(Optional.of(anyString()));

        // when
        UserResponse response = userService.signUp(request);

        // then
        assertThat(
                PasswordEncryptor.isMatch(request.getPassword(), savedUser.getPassword())).isTrue();

        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(UserStatus.PENDING);

        verify(profileStorage, times(1)).store(request.getProfileImage(), request.getUserId());
        verify(userRepository, times(1)).isDuplicatedUserId(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));

    }

    @DisplayName("잘못된 이메일 형식의 요청을 받으면, 예외가 발생한다.")
    @Test
    void signUp_InValidEmail_ExceptionThrown() {
        // given
        UserSignUpServiceRequest request = createSignUpRequest("잘못된 형식의 이메일");

        // when
        when(profileStorage.store(any(), any())).thenReturn(Optional.of(anyString()));

        // then
        assertThatCode(() -> userService.signUp(request))
                .isInstanceOf(NotValidEmailFormatException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST)
                .hasMessage("잘못된 형식의 이메일입니다.");

    }

    @Test
    @DisplayName("중복된 아이디를 입력하면, 회원가입에 실패한다.")
    void signUp_DuplicatedUserId_Fail() {
        // given
        UserSignUpServiceRequest request = createSignUpRequest("email@gmail.com");

        when(userRepository.isDuplicatedUserId(request.getUserId())).thenReturn(true);

        // when, then
        assertThatCode(() -> userService.signUp(request))
                .isInstanceOf(DuplicatedUserIdException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST)
                .hasMessage("중복된 아이디입니다.");
    }

    @Test
    @DisplayName("가입회원의 유효한 아이디와 비밀번호로 로그인할 수 있다.")
    void login_ValidUser_Success() throws Exception {
        //given
        String userId = "userId123";
        String password = "password1234";

        User savedUser = UserFactory.createBasicUser(userId, password);
        when(userRepository.findUserByUserId(userId)).thenReturn(Optional.of(savedUser));

        //when
        userService.login(userId, password);

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
        assertThatCode(() -> userService.login(userId, password))
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
        assertThatCode(() -> userService.login(userId, password))
                .isInstanceOf(
                        LoginFailException.class)
                .hasFieldOrPropertyWithValue(
                        "httpStatus",
                        HttpStatus.BAD_REQUEST)
                .hasMessage("아이디와 비밀번호를 확인해주세요.");

        verify(userRepository, times(1)).findUserByUserId(any(String.class));
        verify(sessionManager, never()).saveUser(any(String.class));
    }

    private UserSignUpServiceRequest createSignUpRequest(String email) {
        return UserSignUpServiceRequest.builder()
                                       .userId("회원아이디")
                                       .password("비밀번호")
                                       .email(email)
                                       .birth(LocalDate.of(2024, 4, 24))
                                       .profileImage(null)
                                       .profileMessage("회원 메세지")
                                       .build();
    }

    private User createUser(UserSignUpServiceRequest request) {
        return User.builder()
                   .id(1L)
                   .basicProfile(BasicProfile.builder()
                                             .userId(request.getUserId())
                                             .password(PasswordEncryptor.encrypt(
                                                     request.getPassword()))
                                             .birth(request.getBirth())
                                             .profileImage("https://testImage.com/test-image.jpg")
                                             .profileMessage(request.getProfileMessage())
                                             .joinedAt(request.getJoinedAt())
                                             .build())
                   .email(new Email(request.getEmail()))
                   .status(UserStatus.PENDING)
                   .build();

    }

}
