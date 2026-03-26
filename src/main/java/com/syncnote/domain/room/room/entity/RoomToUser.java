package com.syncnote.domain.room.room.entity;

import com.syncnote.domain.room.chat.entity.ChatMessage;
import com.syncnote.domain.room.room.type.RoomRole;
import com.syncnote.domain.user.entity.User;
import com.syncnote.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "room_to_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomToUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_to_user_seq")
    @SequenceGenerator(
            name = "room_to_user_seq",
            sequenceName = "room_to_user_seq",
            allocationSize = 100
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column
    @Enumerated(EnumType.STRING)
    private RoomRole role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_chat_id")
    private ChatMessage lastReadChat;

    @Column(name = "last_visited_at")
    private LocalDateTime lastVisitedAt;

    @Builder
    public RoomToUser(
            User user,
            Room room,
            RoomRole role,
            LocalDateTime joinedAt
    ) {
        this.user = user;
        this.room = room;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public void updateLastReadChat(ChatMessage lastReadChat) {
        this.lastReadChat = lastReadChat;
        this.lastVisitedAt = LocalDateTime.now();
    }

    public void updateLastVisitedAt() {
        this.lastVisitedAt = LocalDateTime.now();
    }
}
