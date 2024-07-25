package com.sns.whisper.global.resolver;

import com.sns.whisper.exception.post.UnAuthorizedUserException;

public class GuestUser extends AppUser {

    private static final String GUEST_USERNAME = "guestUser";

    public GuestUser() {
        super(GUEST_USERNAME);
    }

    @Override
    public String getUserId() {
        throw new UnAuthorizedUserException();
    }

    @Override
    public boolean isGuest() {
        return true;
    }
}
