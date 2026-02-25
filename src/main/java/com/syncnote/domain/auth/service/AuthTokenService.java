package com.syncnote.domain.auth.service;

import com.syncnote.domain.auth.dto.JwtDto;
import com.syncnote.domain.auth.dto.RefreshTokenCache;
import com.syncnote.domain.auth.repository.RefreshTokenRedisRepository;
import com.syncnote.domain.user.entity.User;
import com.syncnote.domain.user.repository.UserRepository;
import com.syncnote.global.error.code.AuthErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import com.syncnote.global.utils.JwtClaims;
import com.syncnote.global.utils.JwtProvider;
import com.syncnote.global.utils.TokenHash;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthTokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final UserRepository userRepository;

    @Transactional
    public JwtDto issueTokens(User user, String sessionId, long tokenVersion) {
        JwtDto dto = createJwtDto(user, sessionId, tokenVersion);

        saveRefreshCache(user.getId(), dto.refreshToken(), sessionId, tokenVersion);

        return dto;
    }

    @Transactional
    public JwtDto updateTokens(String refreshTokenStr) {
        JwtClaims claims = jwtProvider.payloadOrNull(refreshTokenStr);

        long userId = claims.userId();
        String sid = claims.sessionId();
        long tokenVersion = claims.tokenVersion();
        String jti = claims.jti();

        String hash = TokenHash.sha256(refreshTokenStr);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(AuthErrorCode.UNAUTHORIZED));

        // TODO: 토큰 버전 업데이트시키기
        JwtDto dto = issueTokens(user, sid, tokenVersion);

        return dto;
    }

    private JwtDto createJwtDto(User user, String sessionId, long tokenVersion) {
        String accessToken = jwtProvider.generateAccessToken(user, sessionId, tokenVersion);
        String refreshToken = jwtProvider.generateRefreshToken(user, sessionId, tokenVersion);

        // === seconds 기준 ===
        long accessValiditySeconds = jwtProvider.getAccessTokenValiditySeconds();
        long refreshValiditySeconds = jwtProvider.getRefreshTokenValiditySeconds();

        // === 현재 시각 ===
        long nowEpochMillis = System.currentTimeMillis();

        // === API 응답용 epoch millis ===
        long accessExpiresAtMillis = nowEpochMillis + (accessValiditySeconds * 1000);
        long refreshExpiresAtMillis = nowEpochMillis + (refreshValiditySeconds * 1000);

        return new JwtDto(
                JwtDto.BEARER,
                accessToken,
                accessExpiresAtMillis,
                accessValiditySeconds * 1000,
                refreshToken,
                refreshExpiresAtMillis,
                refreshValiditySeconds * 1000
        );
    }

    private void saveRefreshCache(long userId, String refreshToken, String sid, long tokenVersion) {
        JwtClaims claims = jwtProvider.payloadOrNull(refreshToken);

        if (claims == null) {
            log.warn("Refresh token claims null: skip cache write userId={}", userId);
            return;
        }

        long refreshValiditySeconds = jwtProvider.getRefreshTokenValiditySeconds();

        RefreshTokenCache cache = RefreshTokenCache.builder()
                .refreshTokenHash(TokenHash.sha256(refreshToken))
                .sessionId(sid)
                .tokenVersion(tokenVersion)
                .jti(claims.jti())
                .issuedAtEpochMs(System.currentTimeMillis())
                .build();

        refreshTokenRedisRepository.save(userId, cache, Duration.ofSeconds(refreshValiditySeconds));
    }
}
