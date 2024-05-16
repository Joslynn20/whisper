package com.sns.whisper.domain.user.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Embeddable
public class BasicProfile {

    @Column(nullable = false, updatable = false, unique = true)
    private String userId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate birth;
    private String profileImage;
    private String profileMessage;
    @Column(nullable = false)
    private LocalDateTime joinedAt;


    protected BasicProfile() {
    }

    @Builder
    private BasicProfile(String userId, String password, LocalDate birth, String profileImage,
            String profileMessage, LocalDateTime joinedAt) {
        this.userId = userId;
        this.password = password;
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

    public LocalDate getBirth() {
        return birth;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getProfileMessage() {
        return profileMessage;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
