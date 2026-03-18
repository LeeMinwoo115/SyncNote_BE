package com.syncnote.domain.room.note.dto.response;

import java.time.LocalDateTime;

public record NoteDetail(
        Long id,
        String content,
        LocalDateTime updatedAt
) {
}
