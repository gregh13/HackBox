package com.isitcake.game.controller;

import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {
    GameSessionService gameSessionService;

    @Autowired
    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getGameSession(@PathVariable String sessionId) {
        System.out.printf("Retrieving game session '%s'%n", sessionId);

        GameSessionResponseDto gameSessionResponseDto = gameSessionService.getRequestGameSession(sessionId);
        return new ResponseEntity<>(gameSessionResponseDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        System.out.println("Game created by: " + playerName);

        GameSessionResponseDto gameSessionResponseDto = gameSessionService.createGameSession(playerName);
        return new ResponseEntity<>(gameSessionResponseDto, HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        String sessionId = payload.get("sessionId");
        System.out.printf("Player '%s' wants to join game session '%s'%n", playerName, sessionId);

        GameSessionResponseDto gameSessionResponseDto = gameSessionService.joinGameSession(playerName, sessionId);
        System.out.println("GameSessionDto: " + gameSessionResponseDto);
        return new ResponseEntity<>(gameSessionResponseDto, HttpStatus.OK);
    }
}
