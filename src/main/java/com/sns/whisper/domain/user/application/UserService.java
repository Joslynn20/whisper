package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.dto.UserDtoAssembler;
import com.sns.whisper.domain.user.application.dto.request.AuthUserForUserRequest;
import com.sns.whisper.domain.user.application.dto.request.FollowServiceRequest;
import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.FollowServiceResponse;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.application.dto.response.UserSearchServiceResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.event.user.SignUpRollbackEvent;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.exception.user.FileUploadException;
import com.sns.whisper.exception.user.InvalidUserException;
import com.sns.whisper.global.common.PasswordEncryptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileStorage profileStorage;
    private final ApplicationEventPublisher eventPublisher;

    private static final String USER_ID = "USER_ID";


    public UserResponse signUp(UserSignUpServiceRequest request) {

        if (userRepository.isDuplicatedUserId(request.getUserId())) {
            throw new DuplicatedUserIdException();
        }

        User user = createUser(request);

        eventPublisher.publishEvent(new SignUpRollbackEvent(user));

        User savedUser = userRepository.save(user);

        return UserResponse.of(savedUser);
    }

    private User createUser(UserSignUpServiceRequest request) {

        String profileImage = profileStorage.store(request.getProfileImage(), request.getUserId())
                                            .orElseThrow(FileUploadException::new);

        String encryptedPassword = PasswordEncryptor.encrypt(request.getPassword());

        return User.create(request.getUserId(), encryptedPassword, request.getEmail(),
                request.getBirth(), profileImage, request.getProfileMessage(),
                request.getJoinedAt());
    }

    public FollowServiceResponse followUser(FollowServiceRequest request) {
        User fromUser = findUserByUserId(request.getFromUser());
        User toUser = findUserByUserId(request.getToUser());

        fromUser.follow(toUser);

        return FollowServiceResponse.builder()
                                    .following(fromUser.isFollowing(toUser))
                                    .followerCount(toUser.getFollowerCount())
                                    .build();
    }

    public FollowServiceResponse unfollowUser(FollowServiceRequest serviceRequest) {
        User fromUser = findUserByUserId(serviceRequest.getFromUser());
        User toUser = findUserByUserId(serviceRequest.getToUser());

        fromUser.unfollow(toUser);

        return FollowServiceResponse.builder()
                                    .following(fromUser.isFollowing(toUser))
                                    .followerCount(toUser.getFollowerCount()).
                                    build();
    }

    private User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId)
                             .orElseThrow(InvalidUserException::new);
    }

    public List<UserSearchServiceResponse> searchFollowings(Pageable pageable,
            String from,
            AuthUserForUserRequest authUser) {

        User fromUser = findUserByUserId(from);

        List<User> followings = userRepository.findFollowingsOf(fromUser, pageable);

        return getUserSearchResponse(authUser, followings);
    }

    private List<UserSearchServiceResponse> getUserSearchResponse(AuthUserForUserRequest authUser,
            List<User> followings) {

        if (authUser.isGuest()) {
            return UserDtoAssembler.UserSearchResponse(followings);
        }

        User loginUser = findUserByUserId(authUser.getUserId());

        return UserDtoAssembler.UserSearchResponse(followings, loginUser);

    }
}
