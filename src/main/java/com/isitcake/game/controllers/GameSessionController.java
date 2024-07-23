package com.isitcake.game.controllers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.services.GameSessionService;
import com.isitcake.game.services.GameSessionWebSocketService;
import com.isitcake.game.repositories.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameSessionWebSocketService gameSessionWebSocketService;

    @Autowired
    private EpisodeRepository episodeRepository;

    @GetMapping("/{sessionId}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable String sessionId) {
        Optional<GameSession> gameSession = gameSessionService.findBySessionId(sessionId);
        return gameSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<GameSession> createGameSession(@RequestParam int season, @RequestParam int episodeNumber) {
        try {
            GameSession gameSession = gameSessionService.createGameSession(season, episodeNumber);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return ResponseEntity.ok(gameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/{sessionId}/update-state")
    public ResponseEntity<GameSession> updateGameState(@PathVariable String sessionId, @RequestParam String state) {
        try {
            GameSession gameSession = gameSessionService.updateGameState(sessionId, state);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return ResponseEntity.ok(gameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sessionId}/pause")
    public ResponseEntity<GameSession> pauseGameSession(@PathVariable String sessionId) {
        try {
            GameSession gameSession = gameSessionService.pauseGameSession(sessionId);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return ResponseEntity.ok(gameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sessionId}/resume")
    public ResponseEntity<GameSession> resumeGameSession(@PathVariable String sessionId) {
        try {
            GameSession gameSession = gameSessionService.resumeGameSession(sessionId);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return ResponseEntity.ok(gameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sessionId}/update-episode-start")
    public ResponseEntity<GameSession> updateEpisodeStart(@PathVariable String sessionId, @RequestParam long newEpisodeStart) {
        try {
            Timestamp newEpisodeStartTime = new Timestamp(newEpisodeStart);
            GameSession gameSession = gameSessionService.updateEpisodeStartTime(sessionId, newEpisodeStartTime);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return ResponseEntity.ok(gameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{sessionId}/state")
    public ResponseEntity<String> getGameState(@PathVariable String sessionId) {
        String state = gameSessionService.getGameState(sessionId);
        if ("not found".equals(state)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(state);
        }
    }
}
