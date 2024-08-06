package com.isitcake.game.entities.payloads;

import com.isitcake.game.entities.Player;

import java.util.List;

public record PlayerJoinedResponsePayload(List<Player> players) { }
