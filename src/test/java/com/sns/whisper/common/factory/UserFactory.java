package com.sns.whisper.common.factory;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.global.common.PasswordEncryptor;
import java.util.List;

public class UserFactory {

    public static User user(String userId) {
        return createUser(null, userId);
    }

    public static User user(Long id, String userId) {
        return createUser(id, userId);
    }

    public static User user(String userId, String password) {
        String encryptPassword = PasswordEncryptor.encrypt(password);
        return createUser(null, userId, encryptPassword);
    }

    private static User createUser(Long id, String userId) {
        return MockUser.builder()
                       .id(id)
                       .userId(userId)
                       .build();
    }

    private static User createUser(Long id, String userId, String password) {
        return MockUser.builder()
                       .id(id)
                       .password(password)
                       .userId(userId)
                       .build();
    }

    public static List<User> mockUsers() {
        return List.of(user("testId1"), user("testId2"),
                user("testId3"), user("testId4"), user("testId5"));
    }
}
