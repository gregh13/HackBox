package com.isitcake.game.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.isitcake.game.entities.WebSocketMessage;
import com.isitcake.game.entities.PlayerAction;

@Controller
public class WebSocketController {

    @MessageMapping("/game")
    @SendTo("/topic/game-session")
    public WebSocketMessage handleAction(PlayerAction action) throws Exception {
        System.out.println("WebSocket Controller Reached!");
        return new WebSocketMessage(action.getAction(), action.getPlayerName());
    }
}

