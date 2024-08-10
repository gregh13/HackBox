package com.isitcake.game.entities.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isitcake.game.entities.payloads.TransferHostActionPayload;
import com.isitcake.game.enums.EventType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class TransferHostAction extends PlayerAction {
    TransferHostActionPayload payload;

    @JsonCreator
    public TransferHostAction(@JsonProperty("sessionId") String sessionId,
                              @JsonProperty("eventType") EventType eventType,
                              @JsonProperty("payload") TransferHostActionPayload payload) {
        super(sessionId, eventType);
        this.payload = payload;
    }
}
