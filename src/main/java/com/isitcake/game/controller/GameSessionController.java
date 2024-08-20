package com.isitcake.game.controller;

import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.dto.response.error.ErrorResponseDto;
import com.isitcake.game.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {
    @Autowired
    GameSessionService gameSessionService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getGameSession(@PathVariable String sessionId) {
        System.out.printf("Retrieving game session '%s'%n", sessionId);
        try {
            GameSessionResponseDto gameSessionResponseDto = gameSessionService.getGameSessionDto(gameSessionService.getGameSession(sessionId));
            if (gameSessionResponseDto == null) {
                throw new RuntimeException("Game session is null");
            }
            return new ResponseEntity<>(gameSessionResponseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println("\nRetrieve game error: " + e + "\n");
            e.printStackTrace();
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error: Issue finding game session", HttpStatus.NOT_FOUND.value(), e.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        System.out.println("Game created by: " + playerName);

        try {
            GameSessionResponseDto gameSessionResponseDto = gameSessionService.getGameSessionDto(gameSessionService.createGameSession(playerName));
            if (gameSessionResponseDto == null) {
                throw new RuntimeException("Game Session is null");
            }
            return new ResponseEntity<>(gameSessionResponseDto, HttpStatus.OK);

        } catch (RuntimeException e){
            System.out.println("\nCreate game error: " + e + "\n");
            e.printStackTrace();
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error: Issue creating game session", HttpStatus.NOT_FOUND.value(), e.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        String sessionId = payload.get("sessionId");
        System.out.printf("Player '%s' wants to join game session '%s'%n", playerName, sessionId);

        try {
            GameSessionResponseDto gameSessionResponseDto = gameSessionService.getGameSessionDto(gameSessionService.joinGameSession(playerName, sessionId));
            if (gameSessionResponseDto == null) {
                throw new RuntimeException("Game Session is null");
            }
            return new ResponseEntity<>(gameSessionResponseDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            System.out.println("\nJoin game error: " + e + "\n");
            e.printStackTrace();
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error: Issue joining game session", HttpStatus.NOT_FOUND.value(), e.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

    }
}
