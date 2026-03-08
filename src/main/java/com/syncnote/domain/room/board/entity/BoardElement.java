package com.syncnote.domain.room.board.entity;

import com.syncnote.domain.room.chat.ElementType;
import com.syncnote.domain.user.entity.User;
import com.syncnote.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "board_elements")
@NoArgsConstructor
public class BoardElement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_element_seq")
    @SequenceGenerator(
            name = "board_element_seq",
            sequenceName = "board_element_seq",
            allocationSize = 100
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "element_type")
    ElementType elementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    User updatedBy;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Column(name = "data_json")
    String data;
}
