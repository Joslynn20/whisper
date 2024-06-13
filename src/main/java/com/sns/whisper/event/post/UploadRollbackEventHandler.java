package com.sns.whisper.event.post;

import static org.springframework.transaction.event.TransactionPhase.AFTER_ROLLBACK;

import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UploadRollbackEventHandler {

    private final ImageStorage imageStorage;

    @Async
    @TransactionalEventListener(phase = AFTER_ROLLBACK)
    public void rollbackUploadImages(UploadRollbackEvent event) {
        List<String> imageUrls = event.getPost()
                                      .getImageUrls();
        imageStorage.deleteImages(imageUrls);
    }
}
