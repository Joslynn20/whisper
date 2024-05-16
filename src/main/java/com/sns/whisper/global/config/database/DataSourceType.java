package com.sns.whisper.global.config.database;

public enum DataSourceType {
    MASTER("master"),
    SLAVE("slave");

    private String name;

    DataSourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
