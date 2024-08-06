package com.isitcake.game.entities.actions;

import com.isitcake.game.entities.payloads.SetupQuestionActionPayload;
import lombok.Value;
import lombok.EqualsAndHashCode;

@Value
@EqualsAndHashCode(callSuper=true)
public class SetupQuestionAction extends PlayerAction {
    SetupQuestionActionPayload payload;
}
