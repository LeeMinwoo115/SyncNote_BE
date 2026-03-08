package com.syncnote.domain.room.room.repository;

import com.syncnote.domain.room.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByInviteCode(String code);
}
