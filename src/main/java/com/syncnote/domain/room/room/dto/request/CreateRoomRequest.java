package com.syncnote.domain.room.room.dto.request;

import com.syncnote.domain.room.room.type.RoomVisibility;

public record CreateRoomRequest (
        String title,
        String description,
        RoomVisibility visibility
) {
}
