package com.isitcake.game.controller;

import com.isitcake.game.dto.action.*;
import com.isitcake.game.dto.payload.*;
import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.WebSocketMessage;
import com.isitcake.game.service.GameSessionService;
import com.isitcake.game.service.PlayerService;
import com.isitcake.game.type.EventType;
import com.isitcake.game.type.QuestionType;
import com.isitcake.game.type.StateType;
import com.isitcake.game.util.PayloadConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {
    GameSessionService gameSessionService;
    PlayerService playerService;
    SimpMessagingTemplate template;

    @Autowired
    public WebSocketController(GameSessionService gameSessionService, PlayerService playerService, SimpMessagingTemplate template) {
        this.gameSessionService = gameSessionService;
        this.playerService = playerService;
        this.template = template;
    }

    @MessageMapping("/player-joined")
    public void handlePlayerJoined(PlayerJoinedAction playerJoinedAction) throws Exception {
        String sessionId = playerJoinedAction.getSessionId();
        PlayerJoinedActionPayload requestPayload = playerJoinedAction.getPayload();
        System.out.println("PlayerJoinedAction object: " + playerJoinedAction);
        System.out.println("Player joined payload: " + requestPayload);
        System.out.println("SessionId: " + sessionId);

        List<PlayerResponseDto> playerResponseDtos = playerService.getPlayerDtos(playerService.getPlayers(sessionId));
        PlayerJoinedResponsePayload responsePayload = new PlayerJoinedResponsePayload((playerResponseDtos));
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

        // Update Game Session and Players
        GameSessionResponseDto gameSessionResponseDto = gameSessionService.transitionToQuestion(setupQuestionAction.getSessionId(), StateType.QUESTION);

        // Create response payload using converter
        SetupQuestionResponsePayload responsePayload = PayloadConverter.toSetUpQuestionResponsePayload(requestPayload, gameSessionResponseDto.getQuestionId());
        WebSocketMessage<SetupQuestionResponsePayload> message = new WebSocketMessage<>(
                setupQuestionAction.getSessionId(),
                EventType.SETUP_QUESTION.getValue(),
                responsePayload);

        // Send message out to subscribers
        this.template.convertAndSend("/topic/game-session", message);
    }

    @MessageMapping("/submit-answer")
    public void handleSubmitAnswer(SubmitAnswerAction submitAnswerAction) throws Exception {
        SubmitAnswerActionPayload requestPayload = submitAnswerAction.getPayload();
        System.out.println("Submit Answer payload: " + requestPayload);

        String sessionId = submitAnswerAction.getSessionId();

        PlayerResponseDto playerDto = playerService.updatePlayerAnswer(sessionId, requestPayload.playerName(), requestPayload.choice(), requestPayload.timeTaken(), requestPayload.questionId());
        SubmitAnswerResponsePayload responsePayload = new SubmitAnswerResponsePayload(playerDto);
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

        TransitionStateResponsePayload transitionStateResponsePayload;
        GameSessionResponseDto gameSessionResponseDto;

        if (requestPayload.state().equals(StateType.RESULTS)) {
            gameSessionResponseDto = gameSessionService.transitionToResults(sessionId, StateType.RESULTS);
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateAndPlayers(
                    StateType.RESULTS.getValue(),
                    gameSessionResponseDto.getPlayers());
        } else {
            gameSessionResponseDto = gameSessionService.transitionToSetup(sessionId, StateType.SETUP);
            transitionStateResponsePayload = TransitionStateResponsePayload.withStateAndPlayers(
                    StateType.SETUP.getValue(),
                    gameSessionResponseDto.getPlayers());
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

        GameSessionResponseDto gameSessionResponseDto = gameSessionService.transferHost(sessionId, requestPayload.playerName());
        TransferHostResponsePayload responsePayload = new TransferHostResponsePayload(gameSessionResponseDto.getPlayers());

        // Temp PlayerJoined value
        WebSocketMessage<TransferHostResponsePayload> message = new WebSocketMessage<>(sessionId, EventType.PLAYER_JOINED.getValue(), responsePayload);
        this.template.convertAndSend("/topic/game-session", message);
    }
}

