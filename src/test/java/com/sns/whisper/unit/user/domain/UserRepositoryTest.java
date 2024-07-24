package com.sns.whisper.unit.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.global.config.JpaConfiguration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(value = JpaConfiguration.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

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

    @Test
    @DisplayName("회원 아이디로 회원을 조회할 수 있다.")
    void findUserByUserId() throws Exception {
        //given
        String userId = "userId12";
        String password = "password1234";

        User user = UserFactory.createBasicUser(userId, password);
        userRepository.save(user);

        //when
        User savedUser = userRepository.findUserByBasicProfileUserId(userId)
                                       .orElse(null);

        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(userId);
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Nested
    @DisplayName("findFollowingsOf 메소드는")
    class Describe_findFollowingsOf {

        @Nested
        @DisplayName("특정 회원이 팔로잉한 회원이 없을 때")
        class Context_NoFollowings {

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void findFollowingsOf_NoFollowings_EmptyList() throws Exception {
                //given
                User user1 = UserFactory.createBasicUser("testId1");
                User user2 = UserFactory.createBasicUser("testId2");
                User user3 = UserFactory.createBasicUser("testId3");
                User user4 = UserFactory.createBasicUser("testId4");

                userRepository.saveAll(List.of(user1, user2, user3, user4));

                user2.follow(user3);

                testEntityManager.flush();
                testEntityManager.clear();

                //when
                Pageable pageable = PageRequest.of(0, 3);
                List<User> followings = userRepository.findFollowingsOf(user1, pageable);

                //then
                assertThat(followings).isEmpty();
            }
        }

        @Nested
        @DisplayName("특정 회원이 팔로잉한 회원이 있을 때,")
        class Context_ValidFollowings {

            @Test
            @DisplayName("페이징 조건에 따라 회원의 following 목록을 조회할 수 있다.")
            void findFollowingsOf_ValidFollowings_Pageable() throws Exception {
                //given
                User user1 = UserFactory.createBasicUser("testId1");
                User user2 = UserFactory.createBasicUser("testId2");
                User user3 = UserFactory.createBasicUser("testId3");
                User user4 = UserFactory.createBasicUser("testId4");

                userRepository.saveAll(List.of(user1, user2, user3, user4));

                user2.follow(user1);
                user3.follow(user1);
                user4.follow(user1);

                testEntityManager.flush();
                testEntityManager.clear();

                //when
                Pageable pageable = PageRequest.of(0, 3);
                List<User> followings = userRepository.findFollowingsOf(user1, pageable);

                //then
                assertThat(followings)
                        .extracting("basicProfile")
                        .extracting("userId")
                        .containsExactly("testId2", "testId3", "testId4")
                        .hasSize(3);
            }
        }

    }
}
