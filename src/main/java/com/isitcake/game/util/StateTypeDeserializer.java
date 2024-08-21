package com.isitcake.game.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.isitcake.game.type.StateType;

import java.io.IOException;

public class StateTypeDeserializer extends JsonDeserializer<StateType> {
    @Override
    public StateType deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        return StateType.fromValue(value);
    }
}
