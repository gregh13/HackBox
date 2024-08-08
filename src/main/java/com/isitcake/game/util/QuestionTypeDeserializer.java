package com.isitcake.game.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.isitcake.game.enums.QuestionType;

import java.io.IOException;

public class QuestionTypeDeserializer extends JsonDeserializer<QuestionType> {
    @Override
    public QuestionType deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        return QuestionType.fromValue(value);
    }
}
