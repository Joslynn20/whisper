package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.dto.request.SignUpRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
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
    public UserResponse signUp(SignUpRequest request) {
        User savedUser = userRepository.save(createUser(request));
        return UserResponse.from(savedUser);
    }

    private User createUser(SignUpRequest request) {
        String profileImage = profileStorage.store(request.getProfileImage());

        return User.builder()
                   .basicProfile(new BasicProfile(request.getUserId(), request.getPassword(),
                           request.getEmail(), request.getBirth(), profileImage,
                           request.getProfileMessage()))
                   .status(UserStatus.PENDING)
                   .build();
    }
}
