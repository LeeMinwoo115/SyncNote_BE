package com.syncnote.domain.room.room.event;

import com.syncnote.domain.room.room.dto.event.RoomEventDto;
import com.syncnote.global.socket.event.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RoomEventBroadcaster implements Broadcaster<RoomEventDto> {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreated(RoomEventDto event) {
        messagingTemplate.convertAndSend("/topic/room/rooms", event);
    }
}
