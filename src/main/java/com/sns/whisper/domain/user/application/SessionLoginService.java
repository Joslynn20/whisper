package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.session.SessionManager;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.user.LoginFailException;
import com.sns.whisper.global.common.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    @Override
    public void login(String userId, String password) {
        User savedUser = userRepository.findUserByUserId(userId)
                                       .orElseThrow(LoginFailException::new);

        if (!PasswordEncryptor.isMatch(password, savedUser.getPassword())) {
            throw new LoginFailException();
        }

        sessionManager.saveUser(userId);

    }

}
