package com.syncnote.domain.room.chat.repository;

import com.syncnote.domain.room.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("""
        select cm
        from ChatMessage cm
        join fetch cm.user u
        where cm.room.id = :roomId
        order by cm.id desc
    """)
    List<ChatMessage> findLatestMessages(Long roomId, Pageable pageable);

    @Query("""
        select cm
        from ChatMessage cm
        join fetch cm.user u
        where cm.room.id = :roomId
          and cm.id < :cursor
        order by cm.id desc
    """)
    List<ChatMessage> findMessagesBeforeCursor(Long roomId, Long cursor, Pageable pageable);

    long countByRoomIdAndIdGreaterThan(Long roomId, Long lastReadMessageId);

    long countByRoomId(Long roomId);
}
