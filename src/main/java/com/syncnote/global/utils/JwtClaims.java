package com.syncnote.global.utils;

import com.syncnote.global.enums.UserRole;

public record JwtClaims (
    long userId,
    String nickname,
    UserRole role,
    String tokenType,
    String jti,
    String sessionId,
    long tokenVersion
) {
}
