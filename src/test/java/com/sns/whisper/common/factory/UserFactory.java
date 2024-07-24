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

    public static User user(Long id, String userId) {
        return MockUser.builder()
                       .id(id)
                       .userId(userId)
                       .build();
    }

    public static List<User> mockUsers() {
        return List.of(createBasicUser("testId1"), createBasicUser("testId2"),
                createBasicUser("testId3"), createBasicUser("testId4"), createBasicUser("testId5"));
    }
}
