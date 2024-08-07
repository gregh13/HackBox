package com.isitcake.game.controllers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.dtos.ErrorResponseDto;
import com.isitcake.game.entities.dtos.GameSessionDto;
import com.isitcake.game.services.GameSessionService;
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
            GameSessionDto gameSessionDto = gameSessionService.getGameSessionDto(gameSessionService.getGameSession(sessionId));
            if (gameSessionDto == null) {
                throw new RuntimeException("Game session is null");
            }
            return new ResponseEntity<>(gameSessionDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println("\nRetrieve game error: " + e + "\n");
            e.printStackTrace();
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error: Issue finding game session", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        System.out.println("Game created by: " + playerName);

        try {
            GameSessionDto gameSessionDto = gameSessionService.getGameSessionDto(gameSessionService.createGameSession(playerName));
            if (gameSessionDto == null) {
                throw new RuntimeException("Game Session is null");
            }
            return new ResponseEntity<>(gameSessionDto, HttpStatus.OK);

        } catch (RuntimeException e){
            System.out.println("\nCreate game error: " + e + "\n");
            e.printStackTrace();
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error: Issue creating game session", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        String sessionId = payload.get("sessionId");
        System.out.printf("Player '%s' wants to join game session '%s'%n", playerName, sessionId);

        try {
            GameSessionDto gameSessionDto = gameSessionService.getGameSessionDto(gameSessionService.joinGameSession(playerName, sessionId));
            if (gameSessionDto == null) {
                throw new RuntimeException("Game Session is null");
            }
            return new ResponseEntity<>(gameSessionDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            System.out.println("\nJoin game error: " + e + "\n");
            e.printStackTrace();
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error: Issue joining game session", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

    }
}
