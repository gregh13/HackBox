package com.isitcake.game.isitcakefiles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class IsItCakePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int score;
    private long answerTime; // Time taken to answer the question
    private int selectedChoice; // Player's selected choice for the current question
    private boolean isHost; // Indicates if the player is the host

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    @JsonBackReference
    private IsItCakeGameSession isItCakeGameSession;
}
