package com.syncnote.domain.room.room.repository;

import com.syncnote.domain.room.room.dto.response.GetRoomSummary;
import com.syncnote.domain.room.room.entity.RoomToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomToUserRepository extends JpaRepository<RoomToUser, Long> {
    @Query("""
        select new com.syncnote.domain.room.room.dto.response.GetRoomSummary(
            r.id,
            r.title,
            r.description,
            r.visibility,
            r.inviteCode,
            count(allRtu.id),
            r.owner.id
        )
        from RoomToUser myRtu
        join myRtu.room r
        join RoomToUser allRtu on allRtu.room.id = r.id
        where myRtu.user.id = :userId
        group by r.id, r.title, r.description, r.visibility, r.inviteCode, r.owner.id, myRtu.joinedAt
        order by myRtu.joinedAt desc
    """)
    List<GetRoomSummary> findRoomSummariesByUserId(@Param("userId") Long userId);

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
