package com.isitcake.game.dto.action;

import com.isitcake.game.type.EventType;
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
