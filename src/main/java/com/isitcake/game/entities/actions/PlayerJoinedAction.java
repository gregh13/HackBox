package com.isitcake.game.entities.actions;

import com.isitcake.game.entities.payloads.PlayerJoinedActionPayload;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
public class PlayerJoinedAction extends PlayerAction {
    PlayerJoinedActionPayload payload;
}
