package com.isitcake.game.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sessionId;
    private Boolean sessionHost = false;
    private Integer score = 0;
    private String choice = "";
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
