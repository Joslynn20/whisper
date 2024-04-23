package com.sns.whisper.domain.user.domain;

import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.DeleteInfo;
import com.sns.whisper.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    BasicProfile basicProfile;

    @Embedded
    DeleteInfo deleteInfo;

    protected User() {
    }

    public User(BasicProfile basicProfile, DeleteInfo deleteInfo) {
        this.id = null;
        this.basicProfile = basicProfile;
        this.deleteInfo = deleteInfo;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return basicProfile.getUserId();
    }

    public String getPassword() {
        return basicProfile.getPassword();
    }

    public String getEmail() {
        return basicProfile.getEmail();
    }

    public String getProfileImage() {
        return basicProfile.getProfileImage();
    }

    public String getProfileMessage() {
        return basicProfile.getProfileMessage();
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;

        return id != null ? id.equals(user
                .getId()) : user.getId() == null;
    }
}
