package com.isitcake.game.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="session_id")
    private String sessionId;

    @Column(name="session_host")
    private Boolean sessionHost = false;

    @Column(name="score")
    private Integer score = 0;

    @Column(name="choice")
    private String choice = "";

    @Column(name="time_taken")
    private Double timeTaken = 0.0;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    @JsonBackReference
    private GameSession gameSession;

    @Override
    public String toString() {
        return "Player{ " +
                "id=" + id +
                " | name=" + name +
                " | sessionId=" + sessionId +
                " | sessionHost=" + sessionHost +
                " | score=" + score +
                " | choice=" + choice +
                " | timeTaken=" + timeTaken +
                " }";
    }
}
