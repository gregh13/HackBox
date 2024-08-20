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
@Table(name = "game_session")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name="session_id")
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name="game_state")
    private StateType gameState;

    @Column(name="active")
    private Boolean active;

    @Column(name="date_created")
    private Timestamp dateCreated;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Column(name="players")
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
