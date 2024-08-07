package com.isitcake.game.entities.actions;

import com.isitcake.game.enums.EventType;
import lombok.Getter;

@Getter
public abstract class PlayerAction {
    private final String sessionId;
    private final EventType eventType;

    protected PlayerAction(String sessionId, EventType eventType) {
        this.sessionId = sessionId;
        this.eventType = eventType;
    }
}
