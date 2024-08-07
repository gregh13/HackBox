package com.isitcake.game.controllers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.actions.*;
import com.isitcake.game.entities.payloads.*;
import com.isitcake.game.enums.EventType;
import com.isitcake.game.enums.QuestionType;
import com.isitcake.game.enums.StateType;
import com.isitcake.game.mappers.PlayerMapper;
import com.isitcake.game.services.GameSessionService;

import com.isitcake.game.services.PlayerService;
import com.isitcake.game.util.PayloadConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.isitcake.game.entities.WebSocketMessage;

import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private PlayerService playerService;

    private PlayerMapper playerMapper;

    @MessageMapping("/player-joined")
    @SendTo("/topic/game-session")
    public WebSocketMessage<PlayerJoinedResponsePayload> handlePlayerJoined(PlayerJoinedAction playerJoinedAction) throws Exception {
        String sessionId = playerJoinedAction.getSessionId();
        PlayerJoinedActionPayload requestPayload = playerJoinedAction.getPayload();
        System.out.println("Player joined payload: " + requestPayload);
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        if (gameSession == null) {
            //TODO: add exception
            return null;
        }
        List<Player> players = gameSession.getPlayers();
        PlayerJoinedResponsePayload responsePayload = new PlayerJoinedResponsePayload(playerMapper.entitiesToDtos(players));
        return new WebSocketMessage<>(sessionId, EventType.PLAYER_JOINED, responsePayload);
    }

    @MessageMapping("/setup-question")
    @SendTo("/topic/game-session")
    public WebSocketMessage<SetupQuestionResponsePayload> handleSetupQuestion(SetupQuestionAction setupQuestionAction) throws Exception {
        SetupQuestionActionPayload requestPayload = setupQuestionAction.getPayload();
        System.out.println("Setup Question Payload: " + requestPayload);
        if (requestPayload.questionType() == null || requestPayload.timer() == null || (requestPayload.questionType() == QuestionType.CHOICE && requestPayload.choices() == null)) {
            // TODO: add exceptions
            System.out.println("Request Payload is missing required values");
            return null;
        }
        GameSession gameSession = gameSessionService.updateGameState(setupQuestionAction.getSessionId(), StateType.QUESTION);
        if (gameSession == null) {
            //TODO: add exception
            return null;
        }
        SetupQuestionResponsePayload responsePayload = PayloadConverter.toResponsePayload(requestPayload);
        return new WebSocketMessage<>(
                setupQuestionAction.getSessionId(),
                EventType.SETUP_QUESTION,
                responsePayload);
    }

    @MessageMapping("/submit-answer")
    @SendTo("/topic/game-session")
    public WebSocketMessage<SubmitAnswerResponsePayload> handleSubmitAnswer(SubmitAnswerAction submitAnswerAction) throws Exception {
        SubmitAnswerActionPayload requestPayload = submitAnswerAction.getPayload();
        System.out.println("Submit Answer payload: " + requestPayload);

        String sessionId = submitAnswerAction.getSessionId();

        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        if (gameSession == null) {
            //TODO: add exception
            return null;
        }

        Player player = playerService.updatePlayer(requestPayload.playerName(), gameSession, requestPayload.choice(), requestPayload.timeTaken());
        if (player == null) {
            //TODO: add exception
            return null;
        }
        SubmitAnswerResponsePayload responsePayload = new SubmitAnswerResponsePayload(playerMapper.entityToDto(player));
        return new WebSocketMessage<>(sessionId, EventType.SUBMIT_ANSWER, responsePayload);
    }

    @MessageMapping("/transition-state")
    @SendTo("/topic/game-session")
    public WebSocketMessage<TransitionStateResponsePayload> handleTransitionState(TransitionStateAction transitionStateAction) throws Exception {
        TransitionStateActionPayload requestPayload = transitionStateAction.getPayload();
        System.out.println("Transition State Payload: " + requestPayload);

        String sessionId = transitionStateAction.getSessionId();
        GameSession gameSession = gameSessionService.updateGameState(sessionId, requestPayload.state());
        if (gameSession == null) {
            //TODO: add exception
            return null;
        }

        TransitionStateResponsePayload transitionStateResponsePayload;
        if (gameSession.getGameState().equals(StateType.RESULTS)) {
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateAndResults(
                    StateType.RESULTS,
                    playerMapper.entitiesToDtos(gameSession.getPlayers()));
        } else {
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateOnly(StateType.SETUP);
        }

        return new WebSocketMessage<>(
                transitionStateAction.getSessionId(),
                transitionStateAction.getEventType(),
                transitionStateResponsePayload
        );
    }
}

