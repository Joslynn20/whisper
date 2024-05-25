package com.sns.whisper.domain.user.domain.respository;

import com.sns.whisper.domain.user.domain.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    boolean isDuplicatedUserId(String userId);

    Optional<User> findUserByUserId(String userId);
}
