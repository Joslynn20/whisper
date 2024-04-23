package com.sns.whisper.domain.user.domain;

import com.sns.whisper.domain.user.domain.follow.Followers;
import com.sns.whisper.domain.user.domain.follow.Followings;
import com.sns.whisper.domain.user.domain.profile.BasicProfile;
import com.sns.whisper.domain.user.domain.profile.DeleteInfo;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BasicProfile basicProfile;

    @Embedded
    private DeleteInfo deleteInfo;

    @Embedded
    private Followers followers;

    @Embedded
    private Followings followings;

    
}
