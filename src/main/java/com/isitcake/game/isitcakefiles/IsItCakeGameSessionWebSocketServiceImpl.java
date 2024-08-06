package com.isitcake.game.isitcakefiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class IsItCakeGameSessionWebSocketServiceImpl implements IsItCakeGameSessionWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public IsItCakeGameSessionWebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcastGameState(IsItCakeGameSession isItCakeGameSession) {
        messagingTemplate.convertAndSend("/topic/game-session/" + isItCakeGameSession.getSessionId(), isItCakeGameSession);
    }
}
