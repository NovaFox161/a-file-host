package com.novamaday.afilehost.objects;

import java.util.UUID;

public class ApiKey {
    private final String key;
    private UUID userId;
    private int uses;
    private long timeIssued;

    public ApiKey(String _key) {
        key = _key;
    }

    //Getters
    public String getKey() {
        return key;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getUses() {
        return uses;
    }

    public long getTimeIssued() {
        return timeIssued;
    }

    //Setters
    public void setUserId(UUID _id) {
        userId = _id;
    }

    public void setUses(int _uses) {
        uses = _uses;
    }

    public void setTimeIssued(long _time) {
        timeIssued = _time;
    }
}