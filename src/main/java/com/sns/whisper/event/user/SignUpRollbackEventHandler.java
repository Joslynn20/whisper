package com.sns.whisper.event.user;

import com.sns.whisper.domain.user.domain.respository.ProfileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SignUpRollbackEventHandler {

    private final ProfileStorage profileStorage;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void rollbackUploadImage(SignUpRollbackEvent event) {
        String imageUrl = event.getUser()
                               .getProfileImage();
        profileStorage.deleteImage(imageUrl);
    }
}
