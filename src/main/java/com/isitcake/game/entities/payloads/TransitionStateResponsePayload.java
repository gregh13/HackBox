package com.isitcake.game.entities.payloads;

import com.isitcake.game.entities.Player;
import com.isitcake.game.enums.StateType;

import java.util.List;

public record TransitionStateResponsePayload(StateType state, List<Player> results) {
    public static TransitionStateResponsePayload withStateOnly(StateType state) {
        return new TransitionStateResponsePayload(state, List.of());
    }

    public static TransitionStateResponsePayload withStateAndResults(StateType state, List<Player> results) {
        return new TransitionStateResponsePayload(state, results == null ? List.of() : results);
    }
}