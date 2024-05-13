package com.sns.whisper.domain.user.application.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public class UserCreateRequest {

    private String userId;
    private String password;
    private String email;
    private LocalDate birth;
    private MultipartFile profileImage;
    private String profileMessage;
    private LocalDateTime joinedAt;

    @Builder
    private UserCreateRequest(String userId, String password, String email, LocalDate birth,
            MultipartFile profileImage, String profileMessage, LocalDateTime joinedAt) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.profileImage = profileImage;
        this.profileMessage = profileMessage;
        this.joinedAt = joinedAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public String getProfileMessage() {
        return profileMessage;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
}
