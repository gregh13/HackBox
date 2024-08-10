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
    public void handlePlayerJoined(PlayerJoinedAction playerJoinedAction) throws Exception {
        String sessionId = playerJoinedAction.getSessionId();
        PlayerJoinedActionPayload requestPayload = playerJoinedAction.getPayload();
        System.out.println("PlayerJoinedAction object: " + playerJoinedAction);
        System.out.println("Player joined payload: " + requestPayload);
        System.out.println("SessionId: " + sessionId);

        List<Player> players = gameSessionService.getPlayersBySessionId(sessionId);
        PlayerJoinedResponsePayload responsePayload = new PlayerJoinedResponsePayload(playerService.getPlayerDtos(players));
        WebSocketMessage<PlayerJoinedResponsePayload> message = new WebSocketMessage<>(sessionId, EventType.PLAYER_JOINED.getValue(), responsePayload);
        this.template.convertAndSend("/topic/game-session", message);
    }

    @MessageMapping("/setup-question")

    public void handleSetupQuestion(SetupQuestionAction setupQuestionAction) throws Exception {
        SetupQuestionActionPayload requestPayload = setupQuestionAction.getPayload();
        System.out.println("Setup Question Payload: " + requestPayload);
        System.out.println("Setup Question Type: " + requestPayload.questionType());
        if (requestPayload.questionType() == null || requestPayload.timer() == null || (requestPayload.questionType() == QuestionType.CHOICE && requestPayload.choices() == null)) {
            // TODO: add exceptions
            System.out.println("Request Payload is missing required values");
        }
        GameSession gameSession = gameSessionService.updateGameStateAndPlayers(setupQuestionAction.getSessionId(), StateType.QUESTION);
        if (gameSession == null) {
            //TODO: add exception
            System.out.println("Game session could not be found");
        }
        SetupQuestionResponsePayload responsePayload = PayloadConverter.toResponsePayload(requestPayload);
        WebSocketMessage<SetupQuestionResponsePayload> message = new WebSocketMessage<>(
                setupQuestionAction.getSessionId(),
                EventType.SETUP_QUESTION.getValue(),
                responsePayload);
        this.template.convertAndSend("/topic/game-session", message);
    }

    @MessageMapping("/submit-answer")
    public void handleSubmitAnswer(SubmitAnswerAction submitAnswerAction) throws Exception {
        SubmitAnswerActionPayload requestPayload = submitAnswerAction.getPayload();
        System.out.println("Submit Answer payload: " + requestPayload);

        String sessionId = submitAnswerAction.getSessionId();

        Player player = playerService.updatePlayer(requestPayload.playerName(), sessionId, requestPayload.choice(), requestPayload.timeTaken());
        if (player == null) {
            //TODO: add exception
            System.out.println("Player could not be found");
        }
        SubmitAnswerResponsePayload responsePayload = new SubmitAnswerResponsePayload(playerService.getPlayerDto(player));
        WebSocketMessage<SubmitAnswerResponsePayload> message = new WebSocketMessage<>(
                sessionId,
                EventType.SUBMIT_ANSWER.getValue(),
                responsePayload);

        this.template.convertAndSend("/topic/game-session", message);
    }

    @MessageMapping("/transition-state")
    public void handleTransitionState(TransitionStateAction transitionStateAction) throws Exception {
        TransitionStateActionPayload requestPayload = transitionStateAction.getPayload();
        System.out.println("Transition State Payload: " + requestPayload);
        System.out.println("Transition State Type: " + requestPayload.state());

        String sessionId = transitionStateAction.getSessionId();
        GameSession gameSession = gameSessionService.updateGameStateAndPlayers(sessionId, requestPayload.state());
        if (gameSession == null) {
            //TODO: add exception
            System.out.println("Game session update failed");
        }

        TransitionStateResponsePayload transitionStateResponsePayload;
        if (requestPayload.state().equals(StateType.RESULTS)) {
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateAndResults(
                    StateType.RESULTS.getValue(),
                    playerService.getPlayerDtos(gameSessionService.getPlayersBySessionId(sessionId)));
        } else {
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateOnly(StateType.SETUP.getValue());
        }

        WebSocketMessage<TransitionStateResponsePayload> message = new WebSocketMessage<>(
                transitionStateAction.getSessionId(),
                transitionStateAction.getEventType().getValue(),
                transitionStateResponsePayload);

        this.template.convertAndSend("/topic/game-session", message);
    }

    @MessageMapping("/transfer-host")
    public void handleTransferHost(TransferHostAction transferHostAction) throws Exception {
        String sessionId = transferHostAction.getSessionId();
        TransferHostActionPayload requestPayload = transferHostAction.getPayload();
        System.out.println("TransferHostAction object: " + transferHostAction);
        System.out.println("Transfer host payload: " + requestPayload);
        System.out.println("SessionId: " + sessionId);

        TransferHostResponsePayload responsePayload = new TransferHostResponsePayload(playerService.transferHost(sessionId, requestPayload.playerName()));
        WebSocketMessage<TransferHostResponsePayload> message = new WebSocketMessage<>(sessionId, EventType.PLAYER_JOINED.getValue(), responsePayload);
        this.template.convertAndSend("/topic/game-session", message);
    }
}

