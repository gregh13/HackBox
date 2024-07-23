package com.isitcake.game.util;

import java.security.SecureRandom;

public class SessionIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateSessionId() {
        StringBuilder sessionId = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sessionId.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sessionId.toString();
    }
}