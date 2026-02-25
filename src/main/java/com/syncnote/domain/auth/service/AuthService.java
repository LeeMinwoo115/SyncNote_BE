package com.syncnote.domain.auth.service;

import com.syncnote.domain.auth.dto.JwtDto;
import com.syncnote.domain.auth.dto.RefreshTokenCache;
import com.syncnote.domain.auth.dto.request.LoginRequest;
import com.syncnote.domain.auth.dto.request.SignupRequest;
import com.syncnote.domain.auth.dto.response.AuthResponse;
import com.syncnote.domain.auth.dto.response.TokenResponse;
import com.syncnote.domain.auth.dto.response.UserResponse;
import com.syncnote.domain.auth.repository.RefreshTokenRedisRepository;
import com.syncnote.domain.user.entity.User;
import com.syncnote.domain.user.repository.UserRepository;
import com.syncnote.global.enums.UserRole;
import com.syncnote.global.error.code.AuthErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import com.syncnote.global.http.HttpRequestContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final HttpRequestContext httpRequestContext;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public AuthResponse signup(SignupRequest request, UserRole role) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ErrorException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }

        String encoded = passwordEncoder.encode(request.password());

        User user = User.builder()
                .email(request.email())
                .password(encoded)
                .nickname(request.nickname())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        JwtDto tokens = authTokenService.issueTokens(savedUser, UUID.randomUUID().toString(), 1);

        httpRequestContext.setAccessTokenCookie(tokens.accessToken());
        httpRequestContext.setRefreshTokenCookie(tokens.refreshToken());

        return buildAuthResponse(savedUser, tokens);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.email())
                .orElseThrow(() -> new ErrorException(AuthErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ErrorException(AuthErrorCode.LOGIN_FAILED);
        }

        RefreshTokenCache cache = refreshTokenRedisRepository.find(user.getId())
                .orElseThrow(() -> new ErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

        JwtDto tokens = authTokenService.issueTokens(user, UUID.randomUUID().toString(), cache.getTokenVersion() + 1);

        httpRequestContext.setAccessTokenCookie(tokens.accessToken());
        httpRequestContext.setRefreshTokenCookie(tokens.refreshToken());

        return buildAuthResponse(user, tokens);
    }

    private AuthResponse buildAuthResponse(User user, JwtDto tokens) {
        TokenResponse tokenResponse = new TokenResponse(
                tokens.tokenType(),
                tokens.accessToken(),
                tokens.accessTokenExpiresAt(),
                tokens.refreshToken(),
                tokens.refreshTokenExpiresAt()
        );

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole()
        );

        return new AuthResponse(tokenResponse, userResponse);
    }
}
