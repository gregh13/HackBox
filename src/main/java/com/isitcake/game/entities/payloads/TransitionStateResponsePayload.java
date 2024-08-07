package com.isitcake.game.entities.payloads;

import com.isitcake.game.entities.dtos.PlayerDto;
import com.isitcake.game.enums.StateType;

import java.util.List;

public record TransitionStateResponsePayload(StateType state, List<PlayerDto> results) {
    public static TransitionStateResponsePayload withStateOnly(StateType state) {
        return new TransitionStateResponsePayload(state, List.of());
    }

    public static TransitionStateResponsePayload withStateAndResults(StateType state, List<PlayerDto> results) {
        return new TransitionStateResponsePayload(state, results == null ? List.of() : results);
    }
}