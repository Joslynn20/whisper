package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.dto.request.SignUpRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;

public interface UserService {

    public UserResponse signUp(SignUpRequest request);
}
