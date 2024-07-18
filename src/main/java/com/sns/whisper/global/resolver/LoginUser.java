package com.sns.whisper.global.resolver;

public class LoginUser extends AppUser {

    public LoginUser(String userId) {
        super(userId);
    }


    @Override
    public boolean isGuest() {
        return false;
    }
}
