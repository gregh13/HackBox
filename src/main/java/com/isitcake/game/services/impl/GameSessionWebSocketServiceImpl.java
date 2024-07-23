package com.isitcake.game.services.impl;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.services.GameSessionWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameSessionWebSocketServiceImpl implements GameSessionWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameSessionWebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcastGameState(GameSession gameSession) {
        messagingTemplate.convertAndSend("/topic/game-session/" + gameSession.getSessionId(), gameSession);
    }
}
