package com.sns.whisper.common.factory;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.global.common.PasswordEncryptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserFactory {

    public static User createBasicUser(String userId) {

        String encryptPassword = PasswordEncryptor.encrypt("password1234");

        return User.create(userId, encryptPassword, "email@gmail.com", LocalDate.of(1998, 11, 12),
                "basic_profile.png",
                "프로필 메세지", LocalDateTime.now());
    }

    public static User createBasicUser(String userId, String password) {

        String encryptPassword = PasswordEncryptor.encrypt(password);

        return User.create(userId, encryptPassword, "email@gmail.com", LocalDate.of(1998, 11, 12),
                "basic_profile.png",
                "프로필 메세지", LocalDateTime.now());
    }

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
