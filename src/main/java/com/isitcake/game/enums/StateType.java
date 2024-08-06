package com.isitcake.game.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum StateType {
    SETUP("setup"),
    RESULTS("results"),
    QUESTION("question");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}

