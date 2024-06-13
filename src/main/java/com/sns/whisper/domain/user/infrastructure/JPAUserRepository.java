package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAUserRepository extends JpaRepository<User, Long> {

    boolean existsByBasicProfileUserId(String userId);

    Optional<User> findUserByBasicProfileUserId(String userId);
}
