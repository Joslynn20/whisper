package com.sns.whisper.event.user;

import com.sns.whisper.domain.user.domain.User;
import lombok.Getter;

@Getter
public class SignUpRollbackEvent {

    private User user;

    public SignUpRollbackEvent(User user) {
        this.user = user;
    }
}
