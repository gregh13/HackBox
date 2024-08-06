package com.isitcake.game.entities.actions;

import com.isitcake.game.entities.payloads.TransitionStateActionPayload;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class TransitionStateAction extends PlayerAction {
    TransitionStateActionPayload payload;
}
