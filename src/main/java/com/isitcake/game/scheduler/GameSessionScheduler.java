package com.isitcake.game.scheduler;

import com.isitcake.game.services.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GameSessionScheduler {

    @Autowired
    private GameSessionService gameSessionService;

    private ScheduledExecutorService scheduler;

    @PostConstruct
    public void start() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateGameSessions, 0, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stop() {
        scheduler.shutdown();
    }

    private void updateGameSessions() {
        gameSessionService.updateAllGameSessions();
    }
}
