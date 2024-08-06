//package com.isitcake.game.isitcakefiles;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import lombok.Data;
//
//import java.util.List;
//
//@Entity
//@Data
//public class IsItCakeEpisode {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private int season;
//    private int episodeNumber;
//    private String description;
//
//    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<IsItCakeQuestion> isItCakeQuestions;
//
//    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL)
//    @JsonBackReference
//    private List<IsItCakeGameSession> isItCakeGameSessions;
//
//    @Override
//    public String toString() {
//        return "Episode{" +
//                "id=" + id +
//                ", season=" + season +
//                ", episodeNumber=" + episodeNumber +
//                ", description='" + description + '\'' +
//                '}';
//    }
//}
