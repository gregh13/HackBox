package com.isitcake.game.dto.payload;

import com.isitcake.game.dto.response.PlayerResponseDto;

import java.util.List;

public record PlayerJoinedResponsePayload(List<PlayerResponseDto> players) { }
