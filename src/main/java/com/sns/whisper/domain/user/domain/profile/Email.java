package com.sns.whisper.domain.user.domain.profile;

import com.sns.whisper.exception.user.NotValidEmailFormatException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Email {

    // gmail만 허용
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "gmail.com";
    @Column(nullable = false)
    private String email;

    public String getEmail() {
        return email;
    }

    protected Email() {
    }

    public Email(String email) {
        if (isNotValidEmailFormat(email)) {
            throw new NotValidEmailFormatException();
        }
        this.email = email;
    }

    private boolean isNotValidEmailFormat(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }
}
