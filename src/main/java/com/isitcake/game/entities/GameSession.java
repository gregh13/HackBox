package com.isitcake.game.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Episode episode;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Player> players;

    @Transient
    private LinkedList<Object> eventTimeline; // Contains both questions and events

    @Transient
    private Object currentEvent; // Can be Question or Event

    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", currentState='" + currentState + '\'' +
                ", episodeStartTime=" + episodeStartTime +
                ", pausedStartTime=" + pausedStartTime +
                ", isPaused=" + isPaused +
                ", isActive=" + isActive +
                '}';
    }
}
