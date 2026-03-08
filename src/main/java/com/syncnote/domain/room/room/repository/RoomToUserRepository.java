package com.syncnote.domain.room.room.repository;

import com.syncnote.domain.room.room.entity.RoomToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomToUserRepository extends JpaRepository<RoomToUser, Long> {
    @Query("""
        select rtu
        from RoomToUser rtu
        join fetch rtu.room r
        where rtu.user.id = :userId
        order by rtu.joinedAt desc
    """)
    List<RoomToUser> findAllByUserIdWithRoom(@Param("userId") Long userId);
}
