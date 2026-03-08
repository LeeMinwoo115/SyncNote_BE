package com.syncnote.domain.room.room.entity;

import com.syncnote.domain.room.room.type.RoomVisibility;
import com.syncnote.domain.user.entity.User;
import com.syncnote.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @SequenceGenerator(
            name = "room_seq",
            sequenceName = "room_seq",
            allocationSize = 100
    )
    private Long id;

    @Column(nullable = false)
    String title;

    @Column
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RoomVisibility visibility;

    @Column
    String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Builder
    public Room(
            String title,
            String description,
            RoomVisibility visibility,
            String inviteCode,
            User owner
    ) {
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.inviteCode = inviteCode;
        this.owner = owner;
    }

    public void updateInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
