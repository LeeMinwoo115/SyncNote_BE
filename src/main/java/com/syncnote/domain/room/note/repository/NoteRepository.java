package com.syncnote.domain.room.note.repository;

import com.syncnote.domain.room.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
