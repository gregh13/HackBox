package com.isitcake.game.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int score;
    private long answerTime; // Time taken to answer the question
    private int selectedChoice; // Player's selected choice for the current question
    private boolean isHost; // Indicates if the player is the host
}
