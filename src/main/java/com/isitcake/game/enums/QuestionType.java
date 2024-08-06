package com.isitcake.game.enums;
 import lombok.Getter;
 import lombok.AllArgsConstructor;

 @Getter
 @AllArgsConstructor
public enum QuestionType {
     TEXT("text"),
     CHOICE("choice");

     private final String value;

     @Override
     public String toString() {
         return this.value;
     }
}
