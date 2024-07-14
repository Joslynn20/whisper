package com.sns.whisper.unit.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.application.dto.request.FollowServiceRequest;
import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.FollowServiceResponse;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.Email;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.exception.user.InvalidUserException;
import com.sns.whisper.exception.user.NotValidEmailFormatException;
import com.sns.whisper.exception.user.SameFromToUserException;
import com.sns.whisper.global.common.PasswordEncryptor;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ProfileStorage profileStorage;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;


    @DisplayName("회원가입 요청을 받으면, 승인 대기 상태의 회원을 생성한다.")
    @Test
    void signUp_ValidUser_ExpectPendingUser() {
        // given
        UserSignUpServiceRequest request = createSignUpRequest("email@gmail.com");

        User savedUser = createUser(request);
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        given(profileStorage.store(any(), any())).willReturn(Optional.of(anyString()));

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

        given(profileStorage.store(any(), any())).willReturn((Optional.of(anyString())));
        // when, then
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
        given(userRepository.isDuplicatedUserId(request.getUserId())).willReturn(true);

        // when, then
        assertThatCode(() -> userService.signUp(request))
                .isInstanceOf(DuplicatedUserIdException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST)
                .hasMessage("중복된 아이디입니다.");
    }

    @DisplayName("followUser 메서드는")
    @Nested
    class Describe_followUser {

        @DisplayName("로그인 하지 않은 회원은")
        @Nested
        class Context_NotLoginUser {

            @Test
            @DisplayName("팔로우할 수 없다.")
            void followUser_NotLoginUser_400Exception() throws Exception {
                //given
                String fromUser = "testId1";
                String toUser = "testId2";

                FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromUser,
                        toUser);

                //when, then
                assertThatCode(() -> userService.followUser(followServiceRequest))
                        .isInstanceOf(InvalidUserException.class)
                        .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
            }
        }

        @DisplayName("존재하지 않는 회원은")
        @Nested
        class Context_InvalidUser {

            @Test
            @DisplayName("팔로우할 수 없다.")
            void followUser_InvalidUser_400Exception() throws Exception {
                //given
                String fromUser = "testId";
                User loginUser = UserFactory.user(1L, "testId");

                String invalidToUser = "testId1";

                given(userRepository.findUserByUserId(fromUser)).willReturn(Optional.of(loginUser));
                given(userRepository.findUserByUserId(invalidToUser)).willReturn(Optional.empty());

                FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromUser,
                        invalidToUser);

                //when, then
                assertThatCode(() -> userService.followUser(followServiceRequest))
                        .isInstanceOf(InvalidUserException.class)
                        .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);

                verify(userRepository, times(1)).findUserByUserId(fromUser);
                verify(userRepository, times(1)).findUserByUserId(invalidToUser);
            }
        }

        @DisplayName("fromUser와 toUser가 동일하다면")
        @Nested
        class Context_SameFromToUser {

            @Test
            @DisplayName("팔로우할 수 없다.")
            void followUser_SameFromToUser_400Exception() throws Exception {
                //given
                String fromUser = "testId";
                String toUser = "testId";

                User from = UserFactory.user(1L, "testId");
                User to = UserFactory.user(1L, "testId");

                FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromUser,
                        toUser);

                given(userRepository.findUserByUserId(fromUser)).willReturn(Optional.of(from));
                given(userRepository.findUserByUserId(toUser)).willReturn(Optional.of(to));

                //when, then
                assertThatCode(() -> userService.followUser(followServiceRequest))
                        .isInstanceOf(SameFromToUserException.class)
                        .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);

                verify(userRepository, times(2)).findUserByUserId(fromUser);
            }

            @DisplayName("회원이 팔로우하지 않은 회원은")
            @Nested
            class Context_NotFollowedUser {

                @Test
                @DisplayName("팔로우할 수 있다.")
                void followUser_NotFollowedUser_Success() throws Exception {
                    //given
                    String fromUser = "testId";
                    String toUser = "testId1";

                    User from = UserFactory.user(1L, "testId");
                    User to = UserFactory.user(2L, "testId1");

                    FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromUser,
                            toUser);

                    given(userRepository.findUserByUserId(fromUser)).willReturn(Optional.of(from));
                    given(userRepository.findUserByUserId(toUser)).willReturn(Optional.of(to));

                    //when
                    FollowServiceResponse response = userService.followUser(followServiceRequest);

                    // then
                    assertThat(response.getFollowerCount()).isEqualTo(1);
                    assertThat(response.isFollowing()).isTrue();

                    verify(userRepository, times(1)).findUserByUserId(fromUser);
                    verify(userRepository, times(1)).findUserByUserId(toUser);
                }
            }

            @DisplayName("회원이 이미 팔로우한 회원은")
            @Nested
            class Context_FollowedUser {

                @Test
                @DisplayName("팔로우할 수 없다.")
                void followUser_FollowedUser_400Exception() throws Exception {
                    //given
                    String fromUser = "testId";
                    String toUser = "testId1";

                    User from = UserFactory.user(1L, "testId");
                    User to = UserFactory.user(2L, "testId1");

                    FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromUser,
                            toUser);

                    from.follow(to);

                    given(userRepository.findUserByUserId(fromUser)).willReturn(Optional.of(from));
                    given(userRepository.findUserByUserId(toUser)).willReturn(Optional.of(to));

                    //when, then
                    assertThatCode(() -> userService.followUser(followServiceRequest))
                            .isInstanceOf(DuplicatedFollowException.class)
                            .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);

                    verify(userRepository, times(1)).findUserByUserId(fromUser);
                    verify(userRepository, times(1)).findUserByUserId(toUser);
                }
            }
        }


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
