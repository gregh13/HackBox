package com.isitcake.game.entities;

import lombok.Data;

@Data
public class Event {
    private String type;
    private long eventTime;

    public Event(String type, long eventTime) {
        this.type = type;
        this.eventTime = eventTime;
    }
}
