package com.sns.whisper.spring.integration.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.application.dto.request.UserCreateRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
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
    private UserService userService;

    @Autowired
    private JPAUserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("회원가입에 성공하면, 회원정보를 반환한다.")
    void signUp_ValidUser_Success() throws Exception {
        //given
        LocalDateTime joinedAt = LocalDateTime.now();
        UserCreateRequest request = UserCreateRequest.builder()
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

        UserCreateRequest request = UserCreateRequest.builder()
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
}
