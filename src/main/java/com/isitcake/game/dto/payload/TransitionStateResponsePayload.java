package com.isitcake.game.dto.payload;

import com.isitcake.game.dto.response.PlayerResponseDto;

import java.util.List;

public record TransitionStateResponsePayload(String state, List<PlayerResponseDto> players) {
    public static TransitionStateResponsePayload withStateOnly(String state) {
        return new TransitionStateResponsePayload(state, List.of());
    }

    public static TransitionStateResponsePayload withStateAndPlayers(String state, List<PlayerResponseDto> players) {
        return new TransitionStateResponsePayload(state, players == null ? List.of() : players);
    }
}