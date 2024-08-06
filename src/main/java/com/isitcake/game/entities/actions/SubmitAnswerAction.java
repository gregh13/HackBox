package com.isitcake.game.entities.actions;

import com.isitcake.game.entities.payloads.SubmitAnswerActionPayload;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class SubmitAnswerAction extends PlayerAction {
    SubmitAnswerActionPayload payload;
}
