package com.isitcake.game.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.isitcake.game.type.StateType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String sessionId;

    @Enumerated(EnumType.STRING)
    private StateType gameState;
    private Boolean active;
    private Timestamp dateCreated;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Player> players  = new ArrayList<>();

    @Override
    public String toString() {
        return "GameSession{ " +
                "id=" + id +
                " | sessionId=" + sessionId +
                " | gameState=" + gameState +
                " | active=" + active +
                " | players=" + players +
                " }";
    }
}
