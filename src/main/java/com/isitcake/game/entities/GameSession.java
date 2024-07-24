package com.isitcake.game.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @Transient
    private LinkedList<Object> eventTimeline; // Contains both questions and events

    @Transient
    private Object currentEvent; // Can be Question or Event
}
