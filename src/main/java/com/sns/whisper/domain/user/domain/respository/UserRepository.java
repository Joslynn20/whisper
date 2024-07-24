package com.sns.whisper.domain.user.domain.respository;

import com.sns.whisper.domain.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserRepository {

    User save(User user);

    boolean isDuplicatedUserId(String userId);

    Optional<User> findUserByUserId(String userId);

    List<User> findFollowingsOf(User toUser, Pageable pageable);
}
