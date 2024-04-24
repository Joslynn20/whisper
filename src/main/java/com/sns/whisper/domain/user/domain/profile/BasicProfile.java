package com.sns.whisper.domain.user.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class BasicProfile {

    @Column(nullable = false, unique = true, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birth;

    private String profileImage;

    private String profileMessage;


    protected BasicProfile() {
    }

    public BasicProfile(String userId, String password, String email, LocalDate birth,
            String profileImage, String profileMessage) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.profileImage = profileImage;
        this.profileMessage = profileMessage;
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

    public String getProfileImage() {
        return profileImage;
    }

    public String getProfileMessage() {
        return profileMessage;
    }
}
