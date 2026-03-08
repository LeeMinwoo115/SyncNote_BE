package com.syncnote.domain.room.room.dto.response;

import com.syncnote.domain.room.room.type.RoomVisibility;

public record GetRoomSummary(
        Long id,
        String title,
        String description,
        RoomVisibility visibility,
        String inviteCode,
        Long ownerId
) {
}
