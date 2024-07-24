package com.sns.whisper.spring.integration.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.application.dto.request.AuthUserForUserRequest;
import com.sns.whisper.domain.user.application.dto.response.UserSearchServiceResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.domain.user.presentation.dto.UserAssembler;
import com.sns.whisper.global.resolver.GuestUser;
import com.sns.whisper.global.resolver.LoginUser;
import com.sns.whisper.spring.integration.IntegrationTest;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class UserQueryServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("로그인 회원은 특정 회원 팔로잉 목록을 조회할 수 있다. - following 여부 true/false, 자기 자신은 null 반환")
    void searchFollowings_LoginUser_Success() throws Exception {
        //given
        List<User> searchUsers = UserFactory.mockUsers();
        userRepository.saveAll(searchUsers);

        User fromUser = UserFactory.createBasicUser("fromUser");
        User loginUser = UserFactory.createBasicUser("loginUser");

        userRepository.saveAll(List.of(fromUser, loginUser));

        fromUser.follow(searchUsers.get(0));
        fromUser.follow(searchUsers.get(1));
        fromUser.follow(searchUsers.get(2));
        fromUser.follow(searchUsers.get(3));
        fromUser.follow(searchUsers.get(4));
        fromUser.follow(loginUser);

        loginUser.follow(searchUsers.get(0));
        loginUser.follow(searchUsers.get(1));
        loginUser.follow(searchUsers.get(2));

        entityManager.flush();
        entityManager.clear();

        AuthUserForUserRequest authUser = UserAssembler.getAuthUser(new LoginUser("loginUser"));

        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<UserSearchServiceResponse> responses = userService.searchFollowings(pageable,
                fromUser.getUserId(), authUser);
        //then
        assertThat(responses.size()).isEqualTo(6);
        assertThat(responses).extracting("userId", "following")
                             .containsExactly(
                                     tuple(searchUsers.get(0)
                                                      .getUserId(), true),
                                     tuple(searchUsers.get(1)
                                                      .getUserId(), true),
                                     tuple(searchUsers.get(2)
                                                      .getUserId(), true),
                                     tuple(searchUsers.get(3)
                                                      .getUserId(), false),
                                     tuple(searchUsers.get(4)
                                                      .getUserId(), false),
                                     tuple(loginUser.getUserId(), null));
    }


    @Test
    @DisplayName("비로그인 회원은 특정 회원 팔로잉 목록을 조회할 수 있다. - following 여부 null 반환")
    void searchFollowings_GuestUser_Success() throws Exception {
        //given
        List<User> searchUsers = UserFactory.mockUsers();
        userRepository.saveAll(searchUsers);

        User fromUser = UserFactory.createBasicUser("fromUser");

        userRepository.save(fromUser);

        fromUser.follow(searchUsers.get(0));
        fromUser.follow(searchUsers.get(1));
        fromUser.follow(searchUsers.get(2));
        fromUser.follow(searchUsers.get(3));
        fromUser.follow(searchUsers.get(4));

        entityManager.flush();
        entityManager.clear();

        AuthUserForUserRequest authUser = UserAssembler.getAuthUser(new GuestUser());

        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<UserSearchServiceResponse> responses = userService.searchFollowings(pageable,
                fromUser.getUserId(), authUser);
        
        //then
        assertThat(responses.size()).isEqualTo(5);
        assertThat(responses).extracting("userId", "following")
                             .containsExactly(
                                     tuple(searchUsers.get(0)
                                                      .getUserId(), null),
                                     tuple(searchUsers.get(1)
                                                      .getUserId(), null),
                                     tuple(searchUsers.get(2)
                                                      .getUserId(), null),
                                     tuple(searchUsers.get(3)
                                                      .getUserId(), null),
                                     tuple(searchUsers.get(4)
                                                      .getUserId(), null));
    }

}
