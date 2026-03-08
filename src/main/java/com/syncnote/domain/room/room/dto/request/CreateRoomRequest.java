package com.syncnote.domain.room.room.dto.request;

import com.syncnote.domain.room.room.type.RoomVisibility;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateRoomRequest (
        String title,

        @Schema(
                description = "방 접근 권한 설정",
                example = "PRIVATE | PUBLIC | INVITE_ONLY"
        )
        RoomVisibility visibility
) {
}
