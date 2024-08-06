//package com.isitcake.game.isitcakefiles;
//
//import com.isitcake.game.util.SessionIdGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.util.*;
//
//@Service
//public class IsItCakeGameSessionServiceImpl implements IsItCakeGameSessionService {
//
//    @Autowired
//    private IsItCakeGameSessionRepository isItCakeGameSessionRepository;
//
//    @Autowired
//    private IsItCakePlayerRepository isItCakePlayerRepository;
//
//    @Autowired
//    private IsItCakeEpisodeRepository isItCakeEpisodeRepository;
//
//    @Autowired
//    private IsItCakeGameSessionWebSocketService isItCakeGameSessionWebSocketService;
//
//    @Override
//    public Optional<IsItCakeGameSession> findBySessionId(String sessionId) {
//        return isItCakeGameSessionRepository.findBySessionId(sessionId);
//    }
//
//    @Override
//    public IsItCakeGameSession createGameSession(int season, int episodeNumber, String playerName) {
//        Optional<IsItCakeEpisode> episodeOptional = isItCakeEpisodeRepository.findBySeasonAndEpisodeNumber(season, episodeNumber);
//        if (episodeOptional.isPresent()) {
//            try {
//                System.out.println("Episode found: " + episodeOptional.get());
//                IsItCakeEpisode isItCakeEpisode = episodeOptional.get();
//                String sessionId = SessionIdGenerator.generateSessionId();
//                IsItCakeGameSession isItCakeGameSession = new IsItCakeGameSession();
//                isItCakeGameSession.setSessionId(sessionId);
//                isItCakeGameSession.setCurrentState("initial");
//                isItCakeGameSession.setEpisodeStartTime(new Timestamp(System.currentTimeMillis()));
//                isItCakeGameSession.setPausedStartTime(null);
//                isItCakeGameSession.setIsPaused(true);
//                isItCakeGameSession.setIsActive(true);
//                isItCakeGameSession.setIsItCakeEpisode(isItCakeEpisode);
//                isItCakeGameSession.setIsItCakePlayers(new ArrayList<>()); // Initialize player list
//                isItCakeGameSession.setEventTimeline(initializeEventTimeline(isItCakeEpisode.getIsItCakeQuestions()));
//                isItCakeGameSession.setCurrentEvent(isItCakeGameSession.getEventTimeline().getFirst());
//                isItCakeGameSession = isItCakeGameSessionRepository.save(isItCakeGameSession);
//                addPlayerToSession(isItCakeGameSession, playerName, true); // Add the player creating the game as the host
//                System.out.println("createGameSession Result: " + isItCakeGameSession);
//                System.out.println("createGameSession Result ID: " + isItCakeGameSession.getSessionId());
//                System.out.println("createGameSession Result Timeline: " + isItCakeGameSession.getEventTimeline());
//                System.out.println("createGameSession Result Players: " + isItCakeGameSession.getIsItCakePlayers());
//                return isItCakeGameSession;
//            } catch (RuntimeException e) {
//                System.out.println("Error occurred with game session creation:\n" + e);
//                return null;
//            }
////            gameSessionWebSocketService.broadcastGameState(gameSession);
//        } else {
//            throw new RuntimeException("Episode not found");
//        }
//    }
//
//    private LinkedList<Object> initializeEventTimeline(List<IsItCakeQuestion> isItCakeQuestions) {
//        LinkedList<Object> timeline = new LinkedList<>();
//        for (IsItCakeQuestion isItCakeQuestion : isItCakeQuestions) {
//            timeline.add(isItCakeQuestion);
//            // Add a result event after each question
//            timeline.add(new IsItCakeEvent("result", isItCakeQuestion.getQuestionEndTime() + 2000L)); // 2 seconds after question ends
//            // Add a waiting period event after each result
//            timeline.add(new IsItCakeEvent("waiting_period", isItCakeQuestion.getQuestionEndTime() + 2000L + 120000L)); // 2 minutes after result
//        }
//        return timeline;
//    }
//
//    @Override
//    public IsItCakeGameSession updateGameState(String sessionId, String newState) {
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            isItCakeGameSession.setCurrentState(newState);
//            isItCakeGameSession = isItCakeGameSessionRepository.save(isItCakeGameSession);
//            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
//            return isItCakeGameSession;
//        } else {
//            throw new RuntimeException("Game session not found");
//        }
//    }
//
//    @Override
//    public String getGameState(String sessionId) {
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        return gameSessionOptional.map(IsItCakeGameSession::getCurrentState).orElse("not found");
//    }
//
//    @Override
//    public IsItCakeGameSession pauseGameSession(String sessionId, String playerName) {
//        if (!isHost(sessionId, playerName)) {
//            throw new RuntimeException("Only the host can pause the game session");
//        }
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            isItCakeGameSession.setPausedStartTime(new Timestamp(System.currentTimeMillis()));
//            isItCakeGameSession.setIsPaused(true);
//            isItCakeGameSession = isItCakeGameSessionRepository.save(isItCakeGameSession);
//            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
//            return isItCakeGameSession;
//        } else {
//            throw new RuntimeException("Game session not found");
//        }
//    }
//
//    @Override
//    public IsItCakeGameSession resumeGameSession(String sessionId, String playerName) {
//        if (!isHost(sessionId, playerName)) {
//            throw new RuntimeException("Only the host can resume the game session");
//        }
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            if (isItCakeGameSession.getIsPaused()) {
//                long pausedDuration = System.currentTimeMillis() - isItCakeGameSession.getPausedStartTime().getTime();
//                Timestamp newEpisodeStartTime = new Timestamp(isItCakeGameSession.getEpisodeStartTime().getTime() + pausedDuration);
//                isItCakeGameSession.setEpisodeStartTime(newEpisodeStartTime);
//                isItCakeGameSession.setPausedStartTime(null);
//                isItCakeGameSession.setIsPaused(false);
//                isItCakeGameSession = isItCakeGameSessionRepository.save(isItCakeGameSession);
//                isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
//                return isItCakeGameSession;
//            }
//            return isItCakeGameSession;
//        } else {
//            throw new RuntimeException("Game session not found");
//        }
//    }
//
//    @Override
//    public IsItCakeGameSession updateEpisodeStartTime(String sessionId, String playerName, Timestamp newEpisodeStartTime) {
//        if (!isHost(sessionId, playerName)) {
//            throw new RuntimeException("Only the host can update the episode start time");
//        }
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            isItCakeGameSession.setEpisodeStartTime(newEpisodeStartTime);
//            isItCakeGameSession = isItCakeGameSessionRepository.save(isItCakeGameSession);
//            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
//            return isItCakeGameSession;
//        } else {
//            throw new RuntimeException("Game session not found");
//        }
//    }
//
//    @Override
//    public void updateAllGameSessions() {
//        List<IsItCakeGameSession> activeSessions = isItCakeGameSessionRepository.findAllActiveSessions();
//        for (IsItCakeGameSession session : activeSessions) {
//            long elapsedTime = System.currentTimeMillis() - session.getEpisodeStartTime().getTime();
//            updateGameStateBasedOnTime(session, elapsedTime);
//            isItCakeGameSessionWebSocketService.broadcastGameState(session);
//        }
//    }
//
//    private void updateGameStateBasedOnTime(IsItCakeGameSession session, long elapsedTime) {
//        Object currentEvent = session.getCurrentEvent();
//        if (currentEvent instanceof IsItCakeQuestion) {
//            IsItCakeQuestion currentIsItCakeQuestion = (IsItCakeQuestion) currentEvent;
//            if (elapsedTime >= currentIsItCakeQuestion.getQuestionEndTime()) {
//                // Move to the next event
//                session.setCurrentEvent(session.getEventTimeline().pollFirst());
//                session.setCurrentState("result");
//            }
//        } else if (currentEvent instanceof IsItCakeEvent) {
//            IsItCakeEvent isItCakeEvent = (IsItCakeEvent) currentEvent;
//            if (isItCakeEvent.getType().equals("result") && elapsedTime >= isItCakeEvent.getEventTime()) {
//                session.setCurrentEvent(session.getEventTimeline().pollFirst());
//                session.setCurrentState("waiting_period");
//            } else if (isItCakeEvent.getType().equals("waiting_period") && elapsedTime >= isItCakeEvent.getEventTime()) {
//                session.setCurrentEvent(session.getEventTimeline().pollFirst());
//                session.setCurrentState("question_" + ((IsItCakeQuestion) session.getCurrentEvent()).getQuestionNumber());
//            }
//        }
//        isItCakeGameSessionRepository.save(session);
//    }
//
//    @Override
//    public IsItCakeGameSession addPlayer(String sessionId, String playerName) {
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            addPlayerToSession(isItCakeGameSession, playerName, false);
//            isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
//            return isItCakeGameSession;
//        } else {
//            throw new RuntimeException("Game session not found");
//        }
//    }
//
//    private void addPlayerToSession(IsItCakeGameSession isItCakeGameSession, String playerName, boolean isHost) {
//        IsItCakePlayer isItCakePlayer = new IsItCakePlayer();
//        isItCakePlayer.setName(playerName);
//        isItCakePlayer.setScore(0);
//        isItCakePlayer.setHost(isHost);
//        isItCakePlayer.setIsItCakeGameSession(isItCakeGameSession);
//        isItCakePlayer = isItCakePlayerRepository.save(isItCakePlayer);
//        isItCakeGameSession.getIsItCakePlayers().add(isItCakePlayer);
//        isItCakeGameSessionRepository.save(isItCakeGameSession);
//    }
//
//    private boolean isHost(String sessionId, String playerName) {
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            return isItCakeGameSession.getIsItCakePlayers().stream()
//                    .anyMatch(isItCakePlayer -> isItCakePlayer.getName().equals(playerName) && isItCakePlayer.isHost());
//        }
//        return false;
//    }
//
//    @Override
//    public IsItCakeGameSession recordPlayerAnswer(String sessionId, String playerName, int selectedChoice, long answerTime) {
//        Optional<IsItCakeGameSession> gameSessionOptional = isItCakeGameSessionRepository.findBySessionId(sessionId);
//        if (gameSessionOptional.isPresent()) {
//            IsItCakeGameSession isItCakeGameSession = gameSessionOptional.get();
//            Optional<IsItCakePlayer> playerOptional = isItCakeGameSession.getIsItCakePlayers().stream()
//                    .filter(isItCakePlayer -> isItCakePlayer.getName().equals(playerName))
//                    .findFirst();
//            if (playerOptional.isPresent()) {
//                IsItCakePlayer isItCakePlayer = playerOptional.get();
//                isItCakePlayer.setSelectedChoice(selectedChoice);
//                isItCakePlayer.setAnswerTime(answerTime);
//                // Logic to update player score if needed
//                isItCakeGameSession = isItCakeGameSessionRepository.save(isItCakeGameSession);
//                isItCakeGameSessionWebSocketService.broadcastGameState(isItCakeGameSession);
//                return isItCakeGameSession;
//            } else {
//                throw new RuntimeException("Player not found");
//            }
//        } else {
//            throw new RuntimeException("Game session not found");
//        }
//    }
//}
