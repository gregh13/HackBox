package com.isitcake.game.controllers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.actions.*;
import com.isitcake.game.entities.payloads.*;
import com.isitcake.game.enums.EventType;
import com.isitcake.game.enums.QuestionType;
import com.isitcake.game.enums.StateType;
import com.isitcake.game.services.GameSessionService;

import com.isitcake.game.services.PlayerService;
import com.isitcake.game.util.PayloadConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.isitcake.game.entities.WebSocketMessage;

import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private PlayerService playerService;
    @Autowired
    private SimpMessagingTemplate template;


    @MessageMapping("/player-joined")
//    @SendTo("/topic/game-session")
    public void handlePlayerJoined(PlayerJoinedAction playerJoinedAction) throws Exception {
        String sessionId = playerJoinedAction.getSessionId();
        PlayerJoinedActionPayload requestPayload = playerJoinedAction.getPayload();
        System.out.println("PlayerJoinedAction object: " + playerJoinedAction);
        System.out.println("Player joined payload: " + requestPayload);
        System.out.println("SessionId: " + sessionId);

        List<Player> players = gameSessionService.getPlayersBySessionId(sessionId);
        PlayerJoinedResponsePayload responsePayload = new PlayerJoinedResponsePayload(playerService.getPlayerDtos(players));
        WebSocketMessage<PlayerJoinedResponsePayload> message = new WebSocketMessage<>(sessionId, EventType.PLAYER_JOINED.getValue(), responsePayload);
        System.out.println("Outgoing Websocket message below: ");
        System.out.println(message);
        System.out.println("Sending message back!");
        this.template.convertAndSend("/topic/game-session", message);
//        return message;
    }

    @MessageMapping("/setup-question")
//    @SendTo("/game-session")
    public void handleSetupQuestion(SetupQuestionAction setupQuestionAction) throws Exception {
        SetupQuestionActionPayload requestPayload = setupQuestionAction.getPayload();
        System.out.println("Setup Question Payload: " + requestPayload);
        if (requestPayload.questionType() == null || requestPayload.timer() == null || (requestPayload.questionType() == QuestionType.CHOICE && requestPayload.choices() == null)) {
            // TODO: add exceptions
            System.out.println("Request Payload is missing required values");
//            return null;
        }
        GameSession gameSession = gameSessionService.updateGameState(setupQuestionAction.getSessionId(), StateType.QUESTION);
        if (gameSession == null) {
            //TODO: add exception
            System.out.println("Game session could not be found");
//            return null;
        }
        SetupQuestionResponsePayload responsePayload = PayloadConverter.toResponsePayload(requestPayload);
        WebSocketMessage<SetupQuestionResponsePayload> message = new WebSocketMessage<>(
                setupQuestionAction.getSessionId(),
                EventType.SETUP_QUESTION.getValue(),
                responsePayload);
        this.template.convertAndSend("/topic/game-session", message);
    }

    @MessageMapping("/submit-answer")
    @SendTo("/game-session")
    public WebSocketMessage<SubmitAnswerResponsePayload> handleSubmitAnswer(SubmitAnswerAction submitAnswerAction) throws Exception {
        SubmitAnswerActionPayload requestPayload = submitAnswerAction.getPayload();
        System.out.println("Submit Answer payload: " + requestPayload);

        String sessionId = submitAnswerAction.getSessionId();

        List<Player> gameSessionPlayers = gameSessionService.getPlayersBySessionId(sessionId);

        Player player = playerService.updatePlayer(requestPayload.playerName(), gameSessionPlayers, requestPayload.choice(), requestPayload.timeTaken());
        if (player == null) {
            //TODO: add exception
            return null;
        }
        SubmitAnswerResponsePayload responsePayload = new SubmitAnswerResponsePayload(playerService.getPlayerDto(player));
        return new WebSocketMessage<>(sessionId, EventType.SUBMIT_ANSWER.getValue(), responsePayload);
    }

    @MessageMapping("/transition-state")
    @SendTo("/game-session")
    public WebSocketMessage<TransitionStateResponsePayload> handleTransitionState(TransitionStateAction transitionStateAction) throws Exception {
        TransitionStateActionPayload requestPayload = transitionStateAction.getPayload();
        System.out.println("Transition State Payload: " + requestPayload);

        String sessionId = transitionStateAction.getSessionId();
        GameSession gameSession = gameSessionService.updateGameState(sessionId, requestPayload.state());
        if (gameSession == null) {
            //TODO: add exception
            System.out.println("Game session update failed");
            return null;
        }

        TransitionStateResponsePayload transitionStateResponsePayload;
        if (gameSession.getGameState().equals(StateType.RESULTS)) {
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateAndResults(
                    StateType.RESULTS,
                    playerService.getPlayerDtos(gameSessionService.getPlayersBySessionId(sessionId)));
        } else {
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateOnly(StateType.SETUP);
        }

        return new WebSocketMessage<>(
                transitionStateAction.getSessionId(),
                transitionStateAction.getEventType().getValue(),
                transitionStateResponsePayload
        );
    }
}

