package com.isitcake.game.isitcakefiles;

import lombok.Data;

@Data
public class IsItCakeEvent {
    private String type;
    private long eventTime;

    public IsItCakeEvent(String type, long eventTime) {
        this.type = type;
        this.eventTime = eventTime;
    }
}
