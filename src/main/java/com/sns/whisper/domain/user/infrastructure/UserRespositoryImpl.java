package com.sns.whisper.domain.user.infrastructure;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import org.springframework.stereotype.Repository;


@Repository
public class UserRespositoryImpl implements UserRepository {

    private final JPAUserRepository jpaUserRepository;

    public UserRespositoryImpl(JPAUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
}
