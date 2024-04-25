package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.dto.request.UserCreateRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.global.common.PasswordEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;

    private final ProfileStorage profileStorage;

    public GeneralUserService(UserRepository userRepository, ProfileStorage profileStorage) {
        this.userRepository = userRepository;
        this.profileStorage = profileStorage;
    }

    @Override
    public UserResponse signUp(UserCreateRequest request) {

        if (userRepository.isDuplicatedUserId(request.getUserId())) {
            throw new DuplicatedUserIdException();
        }

        User user = createUser(request);

        User savedUser = userRepository.save(user);

        return UserResponse.of(savedUser);
    }

    private User createUser(UserCreateRequest request) {
        String profileImage = profileStorage.store(request.getProfileImage());
        String encryptedPassword = PasswordEncryptor.encrypt(request.getPassword());

        return User.create(request.getUserId(), encryptedPassword, request.getEmail(),
                request.getBirth(), profileImage, request.getProfileMessage(),
                request.getJoinedAt());
    }


}
