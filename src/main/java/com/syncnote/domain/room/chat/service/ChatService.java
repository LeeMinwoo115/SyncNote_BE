package com.syncnote.domain.room.chat.service;

import com.syncnote.domain.room.chat.dto.request.ReadChatMessageRequest;
import com.syncnote.domain.room.chat.dto.request.SendChatMessageRequest;
import com.syncnote.domain.room.chat.dto.response.ChatMessageResponse;
import com.syncnote.domain.room.chat.dto.response.GetRoomMessagesResponse;
import com.syncnote.domain.room.chat.dto.response.GetUnreadChatCountResponse;
import com.syncnote.domain.room.chat.entity.ChatMessage;
import com.syncnote.domain.room.chat.repository.ChatMessageRepository;
import com.syncnote.domain.room.room.entity.Room;
import com.syncnote.domain.room.room.entity.RoomToUser;
import com.syncnote.domain.room.room.repository.RoomRepository;
import com.syncnote.domain.room.room.repository.RoomToUserRepository;
import com.syncnote.domain.user.entity.User;
import com.syncnote.domain.user.repository.UserRepository;
import com.syncnote.global.enums.ChatMessageType;
import com.syncnote.global.error.code.RoomErrorCode;
import com.syncnote.global.error.code.UserErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomToUserRepository roomToUserRepository;

    @Transactional
    public ChatMessageResponse sendMessage(SendChatMessageRequest request) {
        long userId = request.userId();

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ErrorException(RoomErrorCode.NOT_FOUND));

        if (!roomToUserRepository.existsByRoomIdAndUserId(room.getId(), userId)) {
            throw new ErrorException(RoomErrorCode.FORBIDDEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(UserErrorCode.NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.builder()
                .room(room)
                .user(user)
                .chatMessageType(ChatMessageType.TEXT)
                .content(request.content())
                .build();

        ChatMessage saved = chatMessageRepository.save(chatMessage);

        long readCount = roomToUserRepository.countByRoomIdAndLastReadChat_IdGreaterThanEqual(
                room.getId(),
                saved.getId()
        );

        return new ChatMessageResponse(
                saved.getId(),
                room.getId(),
                user.getId(),
                user.getNickname(),
                saved.getMessageType(),
                saved.getContent(),
                saved.isEdited(),
                saved.isDeleted(),
                readCount,
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional
    public GetRoomMessagesResponse getMessages(long userId, long roomId, Long cursor, int size) {
        if (!roomToUserRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new ErrorException(RoomErrorCode.FORBIDDEN);
        }

        List<ChatMessage> messages = (cursor == null)
                ? chatMessageRepository.findLatestMessages(roomId, PageRequest.of(0, size))
                : chatMessageRepository.findMessagesBeforeCursor(roomId, cursor, PageRequest.of(0, size));

        if (messages.isEmpty()) {
            return new GetRoomMessagesResponse(Collections.emptyList(), null, false);
        }

        List<ChatMessageResponse> responseMessages = messages.stream()
                .map(message -> {
                    long readCount = roomToUserRepository.countByRoomIdAndLastReadChat_IdGreaterThanEqual(
                            roomId,
                            message.getId()
                    );

                    return new ChatMessageResponse(
                            message.getId(),
                            roomId,
                            message.getUser().getId(),
                            message.getUser().getNickname(),
                            message.getMessageType(),
                            message.getContent(),
                            message.isEdited(),
                            message.isDeleted(),
                            readCount,
                            message.getCreatedAt(),
                            message.getUpdatedAt()
                    );
                })
                .sorted(Comparator.comparing(ChatMessageResponse::id))
                .toList();

        Long nextCursor = messages.stream()
                .map(ChatMessage::getId)
                .min(Long::compareTo)
                .orElse(null);

        boolean hasNext = messages.size() == size;

        return new GetRoomMessagesResponse(responseMessages, nextCursor, hasNext);
    }

    @Transactional
    public void updateLastRead(ReadChatMessageRequest request) {
        long userId = request.userId();

        RoomToUser roomToUser = roomToUserRepository.findByRoomIdAndUserId(request.roomId(), userId)
                .orElseThrow(() -> new ErrorException(RoomErrorCode.FORBIDDEN));

        ChatMessage chatMessage = chatMessageRepository.findById(request.lastReadMessageId())
                .orElseThrow(() -> new ErrorException(RoomErrorCode.NOT_FOUND));

        if (!chatMessage.getRoom().getId().equals(request.roomId())) {
            throw new ErrorException(RoomErrorCode.BAD_REQUEST);
        }

        roomToUser.updateLastReadChat(chatMessage);
    }

    @Transactional
    public GetUnreadChatCountResponse getUnreadCount(long userId, long roomId) {
        RoomToUser roomToUser = roomToUserRepository.findByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ErrorException(RoomErrorCode.FORBIDDEN));

        ChatMessage lastReadMessage = roomToUser.getLastReadChat();

        long unreadCount = 0;

        if (lastReadMessage == null) {
            unreadCount = chatMessageRepository.countByRoomId(roomId);
        } else {
            unreadCount = chatMessageRepository.countByRoomIdAndIdGreaterThan(roomId, lastReadMessage.getId());
        }

        return new GetUnreadChatCountResponse(
                Optional.ofNullable(lastReadMessage)
                        .map(ChatMessage::getId),
                unreadCount
        );
    }
}
