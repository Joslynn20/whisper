package com.sns.whisper.domain.user.application.dto.response;

import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.profile.UserStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;
    private String userId;
    private String email;
    private LocalDate birth;
    private String profileImage;
    private String profileMessage;
    private UserStatus status;

    @Builder
    private UserResponse(Long id, String userId, String email, LocalDate birth, String profileImage,
            String profileMessage, UserStatus status) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.birth = birth;
        this.profileImage = profileImage;
        this.profileMessage = profileMessage;
        this.status = status;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                           .id(user.getId())
                           .userId(user.getUserId())
                           .email(user.getEmail())
                           .birth(user.getBirth())
                           .profileImage(user.getProfileImage())
                           .status(user.getStatus())
                           .build();
    }
}