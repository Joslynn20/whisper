package com.sns.whisper.event.post;

import com.sns.whisper.domain.post.domain.Post;
import lombok.Getter;

@Getter
public class UploadRollbackEvent {

    private Post post;

    public UploadRollbackEvent(Post post) {
        this.post = post;
    }
}
