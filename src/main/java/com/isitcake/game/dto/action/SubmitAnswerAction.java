package com.isitcake.game.dto.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isitcake.game.dto.payload.SubmitAnswerActionPayload;
import com.isitcake.game.type.EventType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class SubmitAnswerAction extends PlayerAction {
    SubmitAnswerActionPayload payload;

    @JsonCreator
    public SubmitAnswerAction(@JsonProperty("sessionId") String sessionId,
                              @JsonProperty("eventType") EventType eventType,
                              @JsonProperty("payload") SubmitAnswerActionPayload payload) {
        super(sessionId, eventType);
        this.payload = payload;
    }
}
