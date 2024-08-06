package com.isitcake.game.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isitcake.game.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage<T> {
    private String sessionId;
    private EventType eventType;
    private T payload;


    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String getPayloadAsString() {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }

    public static <T> T parsePayload(String payload, Class<T> valueType) {
        try {
            return objectMapper.readValue(payload, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize payload", e);
        }
    }
}
