package com.syncnote.domain.room.note.service;

import com.syncnote.domain.room.note.entity.Note;
import com.syncnote.domain.room.note.repository.NoteRepository;
import com.syncnote.domain.room.room.entity.Room;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    @Transactional
    public Note createNote(Room room) {
        Note note = Note.builder().room(room).build();
        return noteRepository.save(note);
    }
}
