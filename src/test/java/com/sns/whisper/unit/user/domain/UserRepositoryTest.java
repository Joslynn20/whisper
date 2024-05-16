package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private JPAUserRepository userRepository;

    @Test
    @DisplayName("아이디가 이미 존재하는지 조회해온다.")
    void isDuplicatedUserId_Success() throws Exception {
        //given
        String userId = "userId";
        String password = "password1234";
        LocalDate birth = LocalDate.of(1999, 11, 30);
        String profileImage = "http://testImages.test/test.jpg";
        String profileMessage = "프로필 메세지";
        String email = "test@gmail.com";

        User user = User.create(userId, password, email, birth, profileImage, profileMessage,
                LocalDateTime.now());

        userRepository.save(user);

        //when
        boolean result = userRepository.existsByBasicProfileUserId(userId);

        //then
        assertThat(result).isTrue();
    }
}
