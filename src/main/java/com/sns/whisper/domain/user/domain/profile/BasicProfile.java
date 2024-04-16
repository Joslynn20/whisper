package com.sns.whisper.domain.user.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
public class BasicProfile {

    @Column(nullable = false, unique = true, updatable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birth;

    private String profileImage;

    private String profileMessage;

    private LocalDateTime lastUploadAt;

    protected BasicProfile() {
    }

    public BasicProfile(String id, String password, String email, LocalDate birth,
            String profileImage,
            String profileMessage, LocalDateTime lastUploadAt) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.profileImage = profileImage;
        this.profileMessage = profileMessage;
        this.lastUploadAt = lastUploadAt;
    }

    public String getId() {
        return id;
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

    public String getProfileImage() {
        return profileImage;
    }

    public String getProfileMessage() {
        return profileMessage;
    }

    public LocalDateTime getLastUploadAt() {
        return lastUploadAt;
    }
    
}
