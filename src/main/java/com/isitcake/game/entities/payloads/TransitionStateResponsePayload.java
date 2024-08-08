package com.isitcake.game.entities.payloads;

import com.isitcake.game.entities.dtos.PlayerDto;
import com.isitcake.game.enums.StateType;

import java.util.List;

public record TransitionStateResponsePayload(String state, List<PlayerDto> results) {
    public static TransitionStateResponsePayload withStateOnly(String state) {
        return new TransitionStateResponsePayload(state, List.of());
    }

    public static TransitionStateResponsePayload withStateAndResults(String state, List<PlayerDto> results) {
        return new TransitionStateResponsePayload(state, results == null ? List.of() : results);
    }
}