package com.syncnote.domain.room.room.service;

import com.syncnote.domain.room.board.service.BoardService;
import com.syncnote.domain.room.note.service.NoteService;
import com.syncnote.domain.room.room.dto.request.CreateRoomRequest;
import com.syncnote.domain.room.room.dto.response.CreateRoomResponse;
import com.syncnote.domain.room.room.dto.response.GetRoomSummary;
import com.syncnote.domain.room.room.entity.Room;
import com.syncnote.domain.room.room.entity.RoomToUser;
import com.syncnote.domain.room.room.repository.RoomRepository;
import com.syncnote.domain.room.room.repository.RoomToUserRepository;
import com.syncnote.domain.room.room.type.RoomRole;
import com.syncnote.domain.user.entity.User;
import com.syncnote.domain.user.repository.UserRepository;
import com.syncnote.global.error.code.UserErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import com.syncnote.global.utils.InviteCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BoardService boardService;
    private final NoteService noteService;
    private final RoomToUserRepository roomToUserRepository;

    @Transactional
    public CreateRoomResponse createRoom(long userId, CreateRoomRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(UserErrorCode.NOT_FOUND));

        Room room = Room.builder()
                .owner(user)
                .title(request.title())
                .description(request.description())
                .visibility(request.visibility())
                .inviteCode(generateInviteCode())
                .build();

        Room savedRoom = roomRepository.saveAndFlush(room);

        boardService.createBoard(savedRoom);
        noteService.createNote(savedRoom);

        RoomToUser roomToUser = RoomToUser.builder()
                .user(user)
                .role(RoomRole.OWNER)
                .room(savedRoom)
                .joinedAt(LocalDateTime.now())
                .build();

        roomToUserRepository.save(roomToUser);

        return new CreateRoomResponse(
                savedRoom.getId(),
                savedRoom.getTitle(),
                savedRoom.getDescription(),
                savedRoom.getVisibility(),
                savedRoom.getInviteCode(),
                savedRoom.getOwner().getId()
        );
    }

    @Transactional
    public List<GetRoomSummary> getRooms(long userId) {
        return roomToUserRepository.findAllByUserIdWithRoom(userId).stream()
                .map(RoomToUser::getRoom)
                .map(room -> new GetRoomSummary(
                        room.getId(),
                        room.getTitle(),
                        room.getDescription(),
                        room.getVisibility(),
                        room.getInviteCode(),
                        room.getOwner().getId()
                ))
                .toList();
    }

    private String generateInviteCode() {
        String code;

        do {
            code = InviteCodeGenerator.generate();
        } while (roomRepository.existsByInviteCode(code));

        return code;
    }
}
