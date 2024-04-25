package com.sns.whisper.domain.user.domain.respository;

import com.sns.whisper.domain.user.domain.User;

public interface UserRepository {

    User save(User user);

    boolean isDuplicatedUserId(String userId);
}
