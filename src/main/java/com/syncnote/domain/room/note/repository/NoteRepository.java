package com.syncnote.domain.room.note.repository;

import com.syncnote.domain.room.note.entity.Note;
import com.syncnote.domain.room.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByRoom(Room room);
}
