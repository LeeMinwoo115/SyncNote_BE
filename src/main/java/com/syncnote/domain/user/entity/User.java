package com.syncnote.domain.user.entity;

import com.syncnote.global.enums.ProviderType;
import com.syncnote.global.enums.UserRole;
import com.syncnote.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 100
    )
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, name = "nickname", length = 20)
    private String nickname;

    @Column
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Builder
    public User(String email, String nickname, String password,
                UserRole role, LocalDateTime deletedAt, ProviderType providerType,
                String providerId) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.deletedAt = deletedAt;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
