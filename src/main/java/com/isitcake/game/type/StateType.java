package com.isitcake.game.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.isitcake.game.util.StateTypeDeserializer;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
@JsonDeserialize(using = StateTypeDeserializer.class)
public enum StateType {
    SETUP("setup"),
    RESULTS("results"),
    QUESTION("question");

    private final String value;
    public static StateType fromValue(String value) {
        for (StateType type : StateType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
    @Override
    public String toString() {
        return this.value;
    }
}

