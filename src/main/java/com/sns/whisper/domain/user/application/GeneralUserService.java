package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;
import com.sns.whisper.domain.user.application.session.SessionManager;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.user.DuplicatedUserIdException;
import com.sns.whisper.exception.user.FileUploadException;
import com.sns.whisper.exception.user.LoginFailException;
import com.sns.whisper.global.common.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneralUserService {

    private final UserRepository userRepository;
    private final ProfileStorage profileStorage;
    private final SessionManager sessionManager;

    private static final String USER_ID = "USER_ID";


    public UserResponse signUp(UserSignUpServiceRequest request) {

        if (userRepository.isDuplicatedUserId(request.getUserId())) {
            throw new DuplicatedUserIdException();
        }

        User user = createUser(request);

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


    public void login(String userId, String password) {
        User savedUser = userRepository.findUserByUserId(userId)
                                       .orElseThrow(LoginFailException::new);

        if (!PasswordEncryptor.isMatch(password, savedUser.getPassword())) {
            throw new LoginFailException();
        }

        sessionManager.saveUser(userId);

    }
}
