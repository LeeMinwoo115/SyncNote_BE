package com.syncnote.domain.room.room.dto.response;

import com.syncnote.domain.room.board.dto.response.BoardDetail;
import com.syncnote.domain.room.note.dto.response.NoteDetail;
import com.syncnote.domain.room.room.type.RoomVisibility;

import java.time.LocalDateTime;
import java.util.List;

public record GetRoomResponse(
        Long id,
        String title,
        String description,
        RoomVisibility visibility,
        String inviteCode,
        Long ownerId,
        LocalDateTime updatedAt,
        List<RoomParticipantResponse> participants,
        NoteDetail note,
        BoardDetail board
) {
}
