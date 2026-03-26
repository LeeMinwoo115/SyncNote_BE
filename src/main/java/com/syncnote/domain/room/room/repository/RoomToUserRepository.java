package com.syncnote.domain.room.room.repository;

import com.syncnote.domain.room.room.entity.RoomToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomToUserRepository extends JpaRepository<RoomToUser, Long> {
    @Query("""
        select rtu
        from RoomToUser rtu
        join fetch rtu.room r
        where rtu.user.id = :userId
        order by rtu.joinedAt desc
    """)
    List<RoomToUser> findAllByUserIdWithRoom(@Param("userId") Long userId);

    boolean existsByRoomIdAndUserId(long roomId, long userId);

    @Query("""
        select rtu
        from RoomToUser rtu
        join fetch rtu.user u
        where rtu.room.id = :roomId
        order by rtu.joinedAt asc
        """)
    List<RoomToUser> findAllByRoomIdWithUser(@Param("roomId") long roomId);

    Optional<RoomToUser> findByRoomIdAndUserId(Long roomId, Long userId);

    long countByRoomIdAndLastReadChat_IdGreaterThanEqual(Long roomId, Long messageId);
}
