package com.isitcake.game.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int questionNumber;
    private int numberChoices;
    private int correctChoice;
    private Long questionStartTime;
    private Long questionEndTime;
    private Long durationMillis;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    private Episode episode;
}
