package com.isitcake.game.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private String currentState;
    private Timestamp episodeStartTime;
    private Timestamp pausedStartTime;
    private Boolean isPaused;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode;
}
