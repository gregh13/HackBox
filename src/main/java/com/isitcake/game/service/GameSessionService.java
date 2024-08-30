package com.isitcake.game.service;


import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;
import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.type.StateType;

import java.util.List;

public interface GameSessionService {

    GameSession getGameSession(String sessionId);

    GameSession getGameSessionWithPlayers(String sessionId);

    GameSessionResponseDto getGameSessionDto(GameSession gameSession);

    GameSessionResponseDto getRequestGameSession(String sessionId);

    GameSessionResponseDto createGameSession(String playerName);

    GameSessionResponseDto joinGameSession(String playerName, String sessionId);

    GameSessionResponseDto transferHost(String sessionId, String playerName);

    GameSessionResponseDto transitionToSetup(String sessionId, StateType gameState);

    GameSessionResponseDto transitionToResults(String sessionId, StateType gameState);

    GameSessionResponseDto transitionToQuestion(String sessionId, StateType gameState);

}
