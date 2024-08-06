package com.isitcake.game.entities.actions;

import com.isitcake.game.enums.EventType;
import lombok.Getter;

@Getter
public abstract class PlayerAction {
    String sessionId;
    private EventType eventType;
}
