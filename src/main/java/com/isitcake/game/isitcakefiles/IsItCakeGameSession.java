//package com.isitcake.game.isitcakefiles;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.sql.Timestamp;
//import java.util.LinkedList;
//import java.util.List;
//
//@Entity
//@Data
//public class IsItCakeGameSession {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String sessionId;
//    private String currentState;
//    private Timestamp episodeStartTime;
//    private Timestamp pausedStartTime;
//    private Boolean isPaused;
//    private Boolean isActive;
//
//    @ManyToOne
//    @JoinColumn(name = "episode_id")
//    @JsonManagedReference
//    private IsItCakeEpisode isItCakeEpisode;
//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<IsItCakePlayer> isItCakePlayers;
//
//    @Transient
//    private LinkedList<Object> eventTimeline; // Contains both questions and events
//
//    @Transient
//    private Object currentEvent; // Can be Question or Event
//
//    @Override
//    public String toString() {
//        return "GameSession{" +
//                "id=" + id +
//                ", sessionId='" + sessionId + '\'' +
//                ", currentState='" + currentState + '\'' +
//                ", episodeStartTime=" + episodeStartTime +
//                ", pausedStartTime=" + pausedStartTime +
//                ", isPaused=" + isPaused +
//                ", isActive=" + isActive +
//                '}';
//    }
//}
