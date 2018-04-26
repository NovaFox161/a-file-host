package com.novamaday.afilehost.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private String username;
    private String email;

    private boolean emailConfirmed;
    private boolean premium;

    private final List<ApiKey> apiKeys = new ArrayList<>();

    public User(UUID _id) {
        id = _id;
    }

    //getters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public boolean isPremium() {
        return premium;
    }

    public List<ApiKey> getApiKeys() {
        return apiKeys;
    }

    //setters
    public void setUsername(String _username) {
        username = _username;
    }

    public void setEmail(String _email) {
        email = _email;
    }

    public void setEmailConfirmed(boolean _confirmed) {
        emailConfirmed = _confirmed;
    }

    public void setPremium(boolean _premium) {
        premium = _premium;
    }
}