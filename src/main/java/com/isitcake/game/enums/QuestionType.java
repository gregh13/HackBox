package com.isitcake.game.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.isitcake.game.util.QuestionTypeDeserializer;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
@JsonDeserialize(using = QuestionTypeDeserializer.class)
public enum QuestionType {
     TEXT("text"),
     CHOICE("choices");

     private final String value;

     public static QuestionType fromValue(String value){
         for (QuestionType type : QuestionType.values()) {
             if (type.getValue().equalsIgnoreCase(value)) {
                 return type;
             }
         }
         throw new IllegalArgumentException("Unknown value: " + value);
     }

     @Override
     public String toString() {
         return this.value;
     }
}
