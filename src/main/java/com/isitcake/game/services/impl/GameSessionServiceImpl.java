package com.isitcake.game.services.impl;

import com.isitcake.game.entities.*;
import com.isitcake.game.repositories.GameSessionRepository;
import com.isitcake.game.repositories.EpisodeRepository;
import com.isitcake.game.repositories.PlayerRepository;
import com.isitcake.game.services.GameSessionService;
import com.isitcake.game.services.GameSessionWebSocketService;
import com.isitcake.game.util.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private GameSessionWebSocketService gameSessionWebSocketService;

    @Override
    public Optional<GameSession> findBySessionId(String sessionId) {
        return gameSessionRepository.findBySessionId(sessionId);
    }

    @Override
    public GameSession createGameSession(int season, int episodeNumber, String playerName) {
        Optional<Episode> episodeOptional = episodeRepository.findBySeasonAndEpisodeNumber(season, episodeNumber);
        if (episodeOptional.isPresent()) {
            try {
                System.out.println("Episode found: " + episodeOptional.get());
                Episode episode = episodeOptional.get();
                String sessionId = SessionIdGenerator.generateSessionId();
                GameSession gameSession = new GameSession();
                gameSession.setSessionId(sessionId);
                gameSession.setCurrentState("initial");
                gameSession.setEpisodeStartTime(new Timestamp(System.currentTimeMillis()));
                gameSession.setPausedStartTime(null);
                gameSession.setIsPaused(true);
                gameSession.setIsActive(true);
                gameSession.setEpisode(episode);
                gameSession.setPlayers(new ArrayList<>()); // Initialize player list
                gameSession.setEventTimeline(initializeEventTimeline(episode.getQuestions()));
                gameSession.setCurrentEvent(gameSession.getEventTimeline().getFirst());
                gameSession = gameSessionRepository.save(gameSession);
                addPlayerToSession(gameSession, playerName, true); // Add the player creating the game as the host
                System.out.println("createGameSession Result: " + gameSession);
                System.out.println("createGameSession Result ID: " + gameSession.getSessionId());
                System.out.println("createGameSession Result Timeline: " + gameSession.getEventTimeline());
                System.out.println("createGameSession Result Players: " + gameSession.getPlayers());
                return gameSession;
            } catch (RuntimeException e) {
                System.out.println("Error occurred with game session creation:\n" + e);
                return null;
            }
//            gameSessionWebSocketService.broadcastGameState(gameSession);
        } else {
            throw new RuntimeException("Episode not found");
        }
    }

    private LinkedList<Object> initializeEventTimeline(List<Question> questions) {
        LinkedList<Object> timeline = new LinkedList<>();
        for (Question question : questions) {
            timeline.add(question);
            // Add a result event after each question
            timeline.add(new Event("result", question.getQuestionEndTime() + 2000L)); // 2 seconds after question ends
            // Add a waiting period event after each result
            timeline.add(new Event("waiting_period", question.getQuestionEndTime() + 2000L + 120000L)); // 2 minutes after result
        }
        return timeline;
    }

    @Override
    public GameSession updateGameState(String sessionId, String newState) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            gameSession.setCurrentState(newState);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public String getGameState(String sessionId) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        return gameSessionOptional.map(GameSession::getCurrentState).orElse("not found");
    }

    @Override
    public GameSession pauseGameSession(String sessionId, String playerName) {
        if (!isHost(sessionId, playerName)) {
            throw new RuntimeException("Only the host can pause the game session");
        }
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            gameSession.setPausedStartTime(new Timestamp(System.currentTimeMillis()));
            gameSession.setIsPaused(true);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public GameSession resumeGameSession(String sessionId, String playerName) {
        if (!isHost(sessionId, playerName)) {
            throw new RuntimeException("Only the host can resume the game session");
        }
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            if (gameSession.getIsPaused()) {
                long pausedDuration = System.currentTimeMillis() - gameSession.getPausedStartTime().getTime();
                Timestamp newEpisodeStartTime = new Timestamp(gameSession.getEpisodeStartTime().getTime() + pausedDuration);
                gameSession.setEpisodeStartTime(newEpisodeStartTime);
                gameSession.setPausedStartTime(null);
                gameSession.setIsPaused(false);
                gameSession = gameSessionRepository.save(gameSession);
                gameSessionWebSocketService.broadcastGameState(gameSession);
                return gameSession;
            }
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public GameSession updateEpisodeStartTime(String sessionId, String playerName, Timestamp newEpisodeStartTime) {
        if (!isHost(sessionId, playerName)) {
            throw new RuntimeException("Only the host can update the episode start time");
        }
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            gameSession.setEpisodeStartTime(newEpisodeStartTime);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public void updateAllGameSessions() {
        List<GameSession> activeSessions = gameSessionRepository.findAllActiveSessions();
        for (GameSession session : activeSessions) {
            long elapsedTime = System.currentTimeMillis() - session.getEpisodeStartTime().getTime();
            updateGameStateBasedOnTime(session, elapsedTime);
            gameSessionWebSocketService.broadcastGameState(session);
        }
    }

    private void updateGameStateBasedOnTime(GameSession session, long elapsedTime) {
        Object currentEvent = session.getCurrentEvent();
        if (currentEvent instanceof Question) {
            Question currentQuestion = (Question) currentEvent;
            if (elapsedTime >= currentQuestion.getQuestionEndTime()) {
                // Move to the next event
                session.setCurrentEvent(session.getEventTimeline().pollFirst());
                session.setCurrentState("result");
            }
        } else if (currentEvent instanceof Event) {
            Event event = (Event) currentEvent;
            if (event.getType().equals("result") && elapsedTime >= event.getEventTime()) {
                session.setCurrentEvent(session.getEventTimeline().pollFirst());
                session.setCurrentState("waiting_period");
            } else if (event.getType().equals("waiting_period") && elapsedTime >= event.getEventTime()) {
                session.setCurrentEvent(session.getEventTimeline().pollFirst());
                session.setCurrentState("question_" + ((Question) session.getCurrentEvent()).getQuestionNumber());
            }
        }
        gameSessionRepository.save(session);
    }

    @Override
    public GameSession addPlayer(String sessionId, String playerName) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            addPlayerToSession(gameSession, playerName, false);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    private void addPlayerToSession(GameSession gameSession, String playerName, boolean isHost) {
        Player player = new Player();
        player.setName(playerName);
        player.setScore(0);
        player.setHost(isHost);
        player.setGameSession(gameSession);
        player = playerRepository.save(player);
        gameSession.getPlayers().add(player);
        gameSessionRepository.save(gameSession);
    }

    private boolean isHost(String sessionId, String playerName) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            return gameSession.getPlayers().stream()
                    .anyMatch(player -> player.getName().equals(playerName) && player.isHost());
        }
        return false;
    }

    @Override
    public GameSession recordPlayerAnswer(String sessionId, String playerName, int selectedChoice, long answerTime) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            Optional<Player> playerOptional = gameSession.getPlayers().stream()
                    .filter(player -> player.getName().equals(playerName))
                    .findFirst();
            if (playerOptional.isPresent()) {
                Player player = playerOptional.get();
                player.setSelectedChoice(selectedChoice);
                player.setAnswerTime(answerTime);
                // Logic to update player score if needed
                gameSession = gameSessionRepository.save(gameSession);
                gameSessionWebSocketService.broadcastGameState(gameSession);
                return gameSession;
            } else {
                throw new RuntimeException("Player not found");
            }
        } else {
            throw new RuntimeException("Game session not found");
        }
    }
}
