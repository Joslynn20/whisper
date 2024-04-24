package com.sns.whisper.unit.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sns.whisper.domain.user.application.GeneralUserService;
import com.sns.whisper.domain.user.application.dto.request.SignUpRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private GeneralUserService userService;

    @Mock
    private ProfileStorage profileStorage;

    @Mock
    private UserRepository userRepository;


    @DisplayName("회원가입 요청을 받으면, 승인 대기 상태의 회원을 생성한다.")
    @Test
    void signUp_ValidUser_ExpectPendingUser() {
        // given
        SignUpRequest request = createSignUpRequest("email@gmail.com");
        User user = createUser(request);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponse response = userService.signUp(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(UserStatus.PENDING);

        verify(profileStorage, times(1)).store(request.getProfileImage());
        verify(userRepository, times(1)).save(any(User.class));

    }

    private SignUpRequest createSignUpRequest(String email) {
        return SignUpRequest.builder()
                            .userId("회원아이디")
                            .password("비밀번호")
                            .birth(LocalDate.of(2024, 4, 24))
                            .profileImage(null)
                            .profileMessage("회원 메세지")
                            .build();
    }

    private User createUser(SignUpRequest request) {
        return User.builder()
                   .id(1L)
                   .basicProfile(new BasicProfile(request.getUserId(), request.getPassword(),
                           request.getEmail(), request.getBirth(),
                           "http://testImages.test/test.jpg",
                           request.getProfileMessage()))
                   .status(UserStatus.PENDING)
                   .build();
    }

}
