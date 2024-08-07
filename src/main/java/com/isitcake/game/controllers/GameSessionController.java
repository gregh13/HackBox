package com.isitcake.game.controllers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.services.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {
    @Autowired
    GameSessionService gameSessionService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable String sessionId) {
        System.out.printf("Retrieving game session '%s'%n", sessionId);
        try {
            GameSession gameSession = gameSessionService.getGameSession(sessionId);
            if (gameSession == null) {
                throw new RuntimeException("Game session is null");
            }
            return ResponseEntity.ok(gameSession);
        } catch (RuntimeException e) {
            System.out.println("\nRetrieve game error: " + e + "\n");
            e.printStackTrace();
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GameSession> createGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        System.out.println("Game created by: " + playerName);

        try {
            GameSession gameSession = gameSessionService.createGameSession(playerName);
            if (gameSession == null) {
                throw new RuntimeException("Game Session is null");
            }
            return ResponseEntity.ok(gameSession);

        } catch (RuntimeException e){
            System.out.println("\nCreate game error: " + e + "\n");
            e.printStackTrace();
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<GameSession> joinGameSession(@RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        String sessionId = payload.get("sessionId");
        System.out.printf("Player '%s' wants to join game session '%s'%n", playerName, sessionId);

        try {
            GameSession gameSession = gameSessionService.joinGameSession(playerName, sessionId);
            if (gameSession == null) {
                throw new RuntimeException("Game Session is null");
            }
            return ResponseEntity.ok(gameSession);

        } catch (RuntimeException e) {
            System.out.println("\nJoin game error: " + e + "\n");
            e.printStackTrace();
            return ResponseEntity.status(404).body(null);
        }

    }
}
