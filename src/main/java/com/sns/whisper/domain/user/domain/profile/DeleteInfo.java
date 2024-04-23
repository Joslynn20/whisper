package com.sns.whisper.domain.user.domain.profile;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class DeleteInfo {

    @ColumnDefault("FALSE")
    private boolean isDeleted;

    private LocalDateTime deleteAt;

    protected DeleteInfo() {
    }

    public DeleteInfo(boolean isDeleted, LocalDateTime deleteAt) {
        this.isDeleted = isDeleted;
        this.deleteAt = deleteAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }
    
}
