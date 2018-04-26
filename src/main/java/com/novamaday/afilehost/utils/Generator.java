package com.novamaday.afilehost.utils;

import com.novamaday.afilehost.crypto.KeyGenerator;
import com.novamaday.afilehost.database.DatabaseManager;
import com.novamaday.afilehost.objects.User;

public class Generator {
    public static String generateEmailConfirmationLink(User user) {
        String code = KeyGenerator.csRandomAlphaNumericString(32);

        //Save to database
        DatabaseManager.getManager().addPendingConfirmation(user, code);

        //TODO: change to the actual domain we will use.
        return "https://afilehost.com/confirm/email?code=" + code;
    }
}
