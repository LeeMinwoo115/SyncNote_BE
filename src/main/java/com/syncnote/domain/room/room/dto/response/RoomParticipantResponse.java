package com.syncnote.domain.room.room.dto.response;

import com.syncnote.domain.room.room.type.RoomRole;

import java.time.LocalDateTime;

public record RoomParticipantResponse(
        Long userId,
        String nickname,
        RoomRole role,
        LocalDateTime joinedAt,
        LocalDateTime lastVisitedAt
) {
}
