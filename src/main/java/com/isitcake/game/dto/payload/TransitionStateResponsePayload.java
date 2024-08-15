package com.isitcake.game.dto.payload;

import com.isitcake.game.dto.response.PlayerResponseDto;

import java.util.List;

public record TransitionStateResponsePayload(String state, List<PlayerResponseDto> results) {
    public static TransitionStateResponsePayload withStateOnly(String state) {
        return new TransitionStateResponsePayload(state, List.of());
    }

    public static TransitionStateResponsePayload withStateAndResults(String state, List<PlayerResponseDto> results) {
        return new TransitionStateResponsePayload(state, results == null ? List.of() : results);
    }
}