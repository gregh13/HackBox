package com.isitcake.game.util;

import java.security.SecureRandom;

public class QuestionIdGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateQuestionId() {
        StringBuilder questionId = new StringBuilder(4);
        for (int i = 0; i < 6; i++) {
            questionId.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return questionId.toString();
    }
}