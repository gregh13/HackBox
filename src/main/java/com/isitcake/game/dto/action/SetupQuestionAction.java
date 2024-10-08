package com.isitcake.game.dto.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isitcake.game.dto.payload.SetupQuestionActionPayload;
import com.isitcake.game.type.EventType;
import lombok.Value;
import lombok.EqualsAndHashCode;

@Value
@EqualsAndHashCode(callSuper=true)
public class SetupQuestionAction extends PlayerAction {
    SetupQuestionActionPayload payload;
    @JsonCreator
    public SetupQuestionAction(@JsonProperty("sessionId") String sessionId,
                              @JsonProperty("eventType") EventType eventType,
                              @JsonProperty("payload") SetupQuestionActionPayload payload) {
        super(sessionId, eventType);
        this.payload = payload;
    }
}
