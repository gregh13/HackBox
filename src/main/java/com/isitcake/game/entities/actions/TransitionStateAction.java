package com.isitcake.game.entities.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isitcake.game.entities.payloads.TransitionStateActionPayload;
import com.isitcake.game.enums.EventType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class TransitionStateAction extends PlayerAction {
    TransitionStateActionPayload payload;
    @JsonCreator
    public TransitionStateAction(@JsonProperty("sessionId") String sessionId,
                               @JsonProperty("eventType") EventType eventType,
                               @JsonProperty("payload") TransitionStateActionPayload payload) {
        super(sessionId, eventType);
        this.payload = payload;
    }
}
