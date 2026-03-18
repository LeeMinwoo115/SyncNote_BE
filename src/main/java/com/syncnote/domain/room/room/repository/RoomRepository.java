package com.syncnote.domain.room.room.repository;

import com.syncnote.domain.room.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByInviteCode(String code);

    Optional<Room> findByInviteCode(String inviteCode);
}
