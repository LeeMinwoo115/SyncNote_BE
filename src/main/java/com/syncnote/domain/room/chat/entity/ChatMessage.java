package com.syncnote.domain.room.chat.entity;

import com.syncnote.domain.room.room.entity.Room;
import com.syncnote.domain.user.entity.User;
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
    Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column
    String content;

    @Builder
    public ChatMessage(
            Room room,
            User user,
            String content
    ) {
        this.room = room;
        this.user = user;
        this.content = content;
    }
}
