package com.isitcake.game.isitcakefiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/game-sessions")
public class IsItCakeGameSessionController {

    @Autowired
    private IsItCakeGameSessionService isItCakeGameSessionService;

    @Autowired
    private IsItCakeGameSessionWebSocketService isItCakeGameSessionWebSocketService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<IsItCakeGameSession> getGameSession(@PathVariable String sessionId) {
        Optional<IsItCakeGameSession> gameSession = isItCakeGameSessionService.findBySessionId(sessionId);
        return gameSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<IsItCakeGameSession> createGameSession(
            @RequestBody Map<String, String> payload) {
        int season = Integer.parseInt(payload.get("season"));
        int episodeNumber = Integer.parseInt(payload.get("episodeNumber"));
        String playerName = payload.get("playerName");
        System.out.println("Season: " + season);
        System.out.println("Episode: " + episodeNumber);
        System.out.println("Player: " + playerName);
        try {
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.createGameSession(season, episodeNumber, playerName);
            System.out.println("Game Session: " + isItCakeGameSession);
            System.out.println("Game Session ID: " + isItCakeGameSession.getSessionId());
            System.out.println("Game Session Players: " + isItCakeGameSession.getIsItCakePlayers());
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            System.out.println("Exception caught: " + e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<IsItCakeGameSession> joinGameSession(
            @RequestBody Map<String, String> payload) {
        String sessionId = payload.get("sessionId");
        String playerName = payload.get("playerName");
        try {
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.addPlayer(sessionId, playerName);
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/{sessionId}/update-state")
    public ResponseEntity<IsItCakeGameSession> updateGameState(
            @PathVariable String sessionId, @RequestBody Map<String, String> payload) {
        String state = payload.get("state");
        try {
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.updateGameState(sessionId, state);
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sessionId}/pause")
    public ResponseEntity<IsItCakeGameSession> pauseGameSession(
            @PathVariable String sessionId, @RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        try {
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.pauseGameSession(sessionId, playerName);
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sessionId}/resume")
    public ResponseEntity<IsItCakeGameSession> resumeGameSession(
            @PathVariable String sessionId, @RequestBody Map<String, String> payload) {
        String playerName = payload.get("playerName");
        try {
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.resumeGameSession(sessionId, playerName);
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sessionId}/update-episode-start")
    public ResponseEntity<IsItCakeGameSession> updateEpisodeStart(
            @PathVariable String sessionId, @RequestBody Map<String, Object> payload) {
        String playerName = (String) payload.get("playerName");
        long newEpisodeStart = ((Number) payload.get("newEpisodeStart")).longValue();
        try {
            Timestamp newEpisodeStartTime = new Timestamp(newEpisodeStart);
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.updateEpisodeStartTime(sessionId, playerName, newEpisodeStartTime);
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{sessionId}/state")
    public ResponseEntity<String> getGameState(@PathVariable String sessionId) {
        String state = isItCakeGameSessionService.getGameState(sessionId);
        if ("not found".equals(state)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(state);
        }
    }

    @PostMapping("/{sessionId}/record-answer")
    public ResponseEntity<IsItCakeGameSession> recordPlayerAnswer(
            @PathVariable String sessionId,
            @RequestBody Map<String, Object> payload) {
        String playerName = (String) payload.get("playerName");
        int selectedChoice = ((Number) payload.get("selectedChoice")).intValue();
        long answerTime = ((Number) payload.get("answerTime")).longValue();
        try {
            IsItCakeGameSession isItCakeGameSession = isItCakeGameSessionService.recordPlayerAnswer(sessionId, playerName, selectedChoice, answerTime);
            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
            return ResponseEntity.ok(isItCakeGameSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
