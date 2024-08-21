package com.isitcake.game.dto.payload;

import com.isitcake.game.entities.dtos.PlayerDto;

import java.util.List;

public record TransferHostResponsePayload(List<PlayerDto> players) { }
