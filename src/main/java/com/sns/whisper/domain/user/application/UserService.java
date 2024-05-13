package com.sns.whisper.domain.user.application;

import com.sns.whisper.domain.user.application.dto.request.UserCreateRequest;
import com.sns.whisper.domain.user.application.dto.response.UserResponse;

public interface UserService {

    UserResponse signUp(UserCreateRequest request);
}
