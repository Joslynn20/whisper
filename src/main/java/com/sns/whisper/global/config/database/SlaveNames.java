package com.sns.whisper.global.config.database;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SlaveNames<T> {

    private final List<T> values;
    private int counter = 0;

    public T getNextName() {
        if (counter >= values.size() - 1) {
            counter = -1;
        }
        return values.get(++counter);
    }
}
