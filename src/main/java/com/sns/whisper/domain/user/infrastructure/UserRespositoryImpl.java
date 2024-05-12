package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UserRespositoryImpl implements UserRepository {

    private final JPAUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public boolean isDuplicatedUserId(String userId) {
        return jpaUserRepository.existsByBasicProfileUserId(userId);
    }
}
