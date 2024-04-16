package com.sns.whisper.domain.user.domain;

import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.DeleteInfo;
import com.sns.whisper.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "user_id")
    private Long no;

    @Embedded
    BasicProfile basicProfile;

    @Embedded
    DeleteInfo deleteInfo;

    protected User() {
    }

    public User(Long no, BasicProfile basicProfile, DeleteInfo deleteInfo) {
        this.no = no;
        this.basicProfile = basicProfile;
        this.deleteInfo = deleteInfo;
    }
}
