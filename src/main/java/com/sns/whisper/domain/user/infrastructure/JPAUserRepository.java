package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAUserRepository extends JpaRepository<User, Long> {

    boolean existsByBasicProfileUserId(String userId);

    Optional<User> findUserByBasicProfileUserId(String userId);

    @Query("select to_User from Follow f join f.toUser to_User on f.fromUser = :fromUser")
    List<User> findFollowingsOf(@Param("fromUser") User fromUser, Pageable pageable);
}
