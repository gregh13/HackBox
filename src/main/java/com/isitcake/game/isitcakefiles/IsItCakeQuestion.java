//package com.isitcake.game.isitcakefiles;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//
//@Entity
//@Data
//public class IsItCakeQuestion {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private int questionNumber;
//    private int numberChoices;
//    private int correctChoice;
//    private Long questionStartTime;
//    private Long questionEndTime;
//    private Long durationMillis;
//
//    @ManyToOne
//    @JoinColumn(name = "episode_id")
//    @JsonBackReference
//    private IsItCakeEpisode isItCakeEpisode;
//
//    @Override
//    public String toString() {
//        return "Question{" +
//                "id=" + id +
//                ", questionNumber=" + questionNumber +
//                ", numberChoices=" + numberChoices +
//                ", correctChoice=" + correctChoice +
//                ", questionStartTime=" + questionStartTime +
//                ", questionEndTime=" + questionEndTime +
//                ", durationMillis=" + durationMillis +
//                '}';
//    }
//}
