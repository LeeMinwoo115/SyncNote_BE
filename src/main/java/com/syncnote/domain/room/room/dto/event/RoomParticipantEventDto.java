package com.syncnote.domain.room.room.dto.event;

import com.syncnote.global.enums.EventEnums;

public record RoomParticipantEventDto(
        Long roomId,
        Long userId,
        String nickname,
        String event
) {
    public static RoomParticipantEventDto joined(Long roomId, Long userId, String nickname) {
        return new RoomParticipantEventDto(roomId, userId, nickname, EventEnums.JOIN.getEvent());
    }
}
