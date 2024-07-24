package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPAUserRepository extends JpaRepository<User, Long> {

    boolean existsByBasicProfileUserId(String userId);

    Optional<User> findUserByBasicProfileUserId(String userId);

    @Query("select from_user from Follow f join f.fromUser from_user on f.toUser = :user")
    List<User> findFollowingsOf(@Param("user") User user, Pageable pageable);
}
