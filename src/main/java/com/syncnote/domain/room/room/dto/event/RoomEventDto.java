package com.syncnote.domain.room.room.dto.event;

import com.syncnote.global.enums.EventEnums;

public record RoomEventDto(
        String event
) {
    public RoomEventDto(EventEnums event) {
        this(event.getEvent());
    }
}
