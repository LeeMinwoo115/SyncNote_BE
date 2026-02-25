package com.syncnote.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenCache implements Serializable {
    private String refreshTokenHash;
    private String sessionId;
    private long tokenVersion;
    private String jti;
    private long issuedAtEpochMs;
}
