package com.syncnote.domain.room.chat.entity;

import com.syncnote.domain.room.room.entity.Room;
import com.syncnote.domain.user.entity.User;
import com.syncnote.global.enums.ChatMessageType;
import com.syncnote.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_messages")
@NoArgsConstructor
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(
            name = "chat_seq",
            sequenceName = "chat_seq",
            allocationSize = 100
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private ChatMessageType messageType;

    @Column
    private String content;

    @Column(name = "is_edited", nullable = false)
    private boolean edited;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Builder
    public ChatMessage(
            Room room,
            User user,
            ChatMessageType chatMessageType,
            String content
    ) {
        this.room = room;
        this.user = user;
        this.messageType = chatMessageType;
        this.content = content;
    }

    public void edit(String content) {
        this.content = content;
        this.edited = true;
    }

    public void delete() {
        this.deleted = true;
        this.content = "";
    }
}
