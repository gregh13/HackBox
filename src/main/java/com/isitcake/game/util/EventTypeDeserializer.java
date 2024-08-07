package com.isitcake.game.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.isitcake.game.enums.EventType;

import java.io.IOException;

public class EventTypeDeserializer extends JsonDeserializer<EventType> {

    @Override
    public EventType deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        return EventType.fromValue(value);
    }
}
