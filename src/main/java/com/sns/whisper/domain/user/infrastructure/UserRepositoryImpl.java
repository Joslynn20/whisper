package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JPAUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public boolean isDuplicatedUserId(String userId) {
        return jpaUserRepository.existsByBasicProfileUserId(userId);
    }

    @Override
    public Optional<User> findUserByUserId(String userId) {
        return jpaUserRepository.findUserByBasicProfileUserId(userId);
    }

    @Override
    public List<User> findFollowingsOf(User fromUser, Pageable pageable) {
        return jpaUserRepository.findFollowingsOf(fromUser, pageable);
    }
}
