package com.isitcake.game.dto.payload;

import com.isitcake.game.dto.response.PlayerResponseDto;

import java.util.List;

public record TransferHostResponsePayload(List<PlayerResponseDto> players) { }
