package com.isitcake.game.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.isitcake.game.util.EventTypeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonDeserialize(using = EventTypeDeserializer.class)
public enum EventType {
    SUBMIT_ANSWER("submitAnswer"),
    PLAYER_JOINED("playerJoined"),
    SETUP_QUESTION("setupQuestion"),
    TRANSITION_STATE("transitionState");

    private final String value;

    public static EventType fromValue(String value) {
        for (EventType type : EventType.values()) {
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
