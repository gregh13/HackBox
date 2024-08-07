package com.isitcake.game.entities.actions;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isitcake.game.entities.payloads.PlayerJoinedActionPayload;
import com.isitcake.game.enums.EventType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class PlayerJoinedAction extends PlayerAction {
    PlayerJoinedActionPayload payload;

    @JsonCreator
    public PlayerJoinedAction(@JsonProperty("sessionId") String sessionId,
                              @JsonProperty("eventType") EventType eventType,
                              @JsonProperty("payload") PlayerJoinedActionPayload payload) {
        super(sessionId, eventType);
        this.payload = payload;
    }
}
