package com.sns.whisper.spring.integration.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.application.dto.request.FollowServiceRequest;
import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.FollowServiceResponse;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.exception.user.DuplicatedFollowException;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.exception.user.InvalidFollowException;
import com.sns.whisper.exception.user.InvalidUserException;
import com.sns.whisper.exception.user.SameFromToUserException;
import com.sns.whisper.spring.integration.IntegrationTest;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


public class UserServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager
                .createNativeQuery(
                        "ALTER TABLE \"user\" ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
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
        assertThatThrownBy(() -> userService.signUp(request)).isInstanceOf(
                                                                     DuplicatedUserIdException.class)
                                                             .hasFieldOrPropertyWithValue(
                                                                     "httpStatus",
                                                                     HttpStatus.BAD_REQUEST)
                                                             .hasMessage("중복된 아이디입니다.");
    }


    @Test
    @DisplayName("회원은 존재하지 않는 회원을 팔로우할 수 없다")
    void followUser_NotExistedUser_400Exception() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId1";

        User fromUser = UserFactory.createBasicUser(fromId);

        userRepository.save(fromUser);

        FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromId, toId);

        //when, then
        assertThatThrownBy(() -> userService.followUser(followServiceRequest))
                .isInstanceOf(InvalidUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("회원은 자기 자신을 팔로우할 수 없다")
    void followUser_SameFromToUser_400Exception() throws Exception {
        //given
        String fromId = "testId";
        String sameId = "testId";

        User fromUser = UserFactory.createBasicUser(fromId);
        userRepository.save(fromUser);

        FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromId, sameId);

        //when, then
        assertThatThrownBy(() -> userService.followUser(followServiceRequest))
                .isInstanceOf(SameFromToUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("회원은 이미 팔로우한 회원을 팔로우할 수 없다")
    void followUser_FollowedUser_400Exception() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId1";

        User fromUser = UserFactory.createBasicUser(fromId);
        User toUser = UserFactory.createBasicUser(toId);

        User from = userRepository.save(fromUser);
        User to = userRepository.save(toUser);

        FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromId, toId);

        userService.followUser(followServiceRequest);

        //when, then
        assertThatThrownBy(() -> userService.followUser(followServiceRequest))
                .isInstanceOf(DuplicatedFollowException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("회원은 팔로우하지 않은 회원을 팔로우할 수 있다")
    void followUser_NotFollowedUser_Success() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId1";

        User fromUser = UserFactory.createBasicUser(fromId);
        User toUser = UserFactory.createBasicUser(toId);

        User from = userRepository.save(fromUser);
        User to = userRepository.save(toUser);

        FollowServiceRequest followServiceRequest = new FollowServiceRequest(fromId, toId);

        //when
        FollowServiceResponse followResponse = userService.followUser(followServiceRequest);

        // then
        assertThat(followResponse).extracting("followerCount", "following")
                                  .contains(1, true);
    }

    @Test
    @DisplayName("회원은 존재하지 않는 회원을 언팔로우할 수 없다.")
    void unfollowUser_NotExistedUser_400Exception() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId1";

        User fromUser = UserFactory.createBasicUser(fromId);

        userRepository.save(fromUser);

        FollowServiceRequest unfollowServiceRequest = new FollowServiceRequest(fromId, toId);

        //when, then
        assertThatThrownBy(() -> userService.unfollowUser(unfollowServiceRequest))
                .isInstanceOf(InvalidUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("회원은 자기 자신을 언팔로우 할 수 없다.")
    void unfollowUser_SameFromToUser_400Exception() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId";

        User fromUser = UserFactory.createBasicUser(fromId);

        userRepository.save(fromUser);

        FollowServiceRequest unfollowServiceRequest = new FollowServiceRequest(fromId, toId);

        //when, then
        assertThatThrownBy(() -> userService.unfollowUser(unfollowServiceRequest)).isInstanceOf(
                                                                                          SameFromToUserException.class)
                                                                                  .hasFieldOrPropertyWithValue(
                                                                                          "httpStatus",
                                                                                          HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("회원은 팔로우하지 않은 회원에 대해 언팔로우 할 수 없다.")
    void unfollowUser_NotFollowedUser_400Exception() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId1";

        User fromUser = UserFactory.createBasicUser(fromId);
        User toUser = UserFactory.createBasicUser(toId);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        FollowServiceRequest unfollowServiceRequest = new FollowServiceRequest(fromId, toId);

        //when, then
        assertThatThrownBy(() -> userService.unfollowUser(unfollowServiceRequest)).isInstanceOf(
                                                                                          InvalidFollowException.class)
                                                                                  .hasFieldOrPropertyWithValue(
                                                                                          "httpStatus",
                                                                                          HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("회원은 팔로우한 회원에 대해서 언팔로우할 수 있다.")
    void unfollowUser_FollowedUser_Success() throws Exception {
        //given
        String fromId = "testId";
        String toId = "testId1";

        User fromUser = UserFactory.createBasicUser(fromId);
        User toUser = UserFactory.createBasicUser(toId);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        fromUser.follow(toUser);

        FollowServiceRequest unfollowServiceRequest = new FollowServiceRequest(fromId, toId);

        //when
        FollowServiceResponse unfollowResponse = userService.unfollowUser(unfollowServiceRequest);

        // then
        assertThat(unfollowResponse).extracting("followerCount", "following")
                                    .contains(0, false);
    }

}
