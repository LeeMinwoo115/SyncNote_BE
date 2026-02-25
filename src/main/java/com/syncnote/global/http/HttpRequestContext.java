package com.syncnote.global.http;

import com.syncnote.global.error.code.AuthErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import com.syncnote.global.security.SecurityUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HttpRequestContext {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final CookieManager cookieManager;

    @Value("${custom.jwt.access-token-duration}")
    private long accessTokenDurationSeconds;

    @Value("${custom.jwt.refresh-token-duration}")
    private long refreshTokenDurationSeconds;

    private Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new ErrorException(AuthErrorCode.UNAUTHORIZED));
    }

    public SecurityUser getSecurityUser() {
        return Optional.of(getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof SecurityUser)
                .map(principal -> (SecurityUser)principal)
                .orElseThrow(() -> new ErrorException(AuthErrorCode.UNAUTHORIZED));
    }

    public Long getUserId() {
        return getSecurityUser().getId();
    }

    public String getCookieValue(String name, String defaultValue) {
        return Optional
                .ofNullable(request.getCookies())
                .flatMap(
                        cookies -> Arrays.stream(cookies)
                                .filter(cookie -> cookie.getName().equals(name))
                                .map(Cookie::getValue)
                                .filter(value -> !value.isBlank())
                                .findFirst()
                )
                .orElse(defaultValue);
    }

    public void setCookie(String name, String value) {
        long maxAgeSec = 0;

        if (value != null && !value.isBlank()) {
            if ("accessToken".equals(name)) {
                maxAgeSec = accessTokenDurationSeconds;
            } else if ("refreshToken".equals(name)) {
                maxAgeSec = refreshTokenDurationSeconds;
            }
        }

        cookieManager.set(request, response, name, value, maxAgeSec);
    }

    public void deleteCookie(String name) {
        cookieManager.delete(request, response, name);
    }

    public void setAccessTokenCookie(String token) {
        cookieManager.setAccessToken(request, response, token, accessTokenDurationSeconds);
    }

    public void setRefreshTokenCookie(String token) {
        cookieManager.setRefreshToken(request, response, token, refreshTokenDurationSeconds);
    }

    public void deleteAccessTokenCookie() {
        cookieManager.deleteAccessToken(request, response);
    }

    public void deleteRefreshTokenCookie() {
        cookieManager.deleteRefreshToken(request, response);
    }

    public void deleteAuthCookies() {
        cookieManager.deleteAuthCookies(request, response);
    }

    public void sendRedirect(String url) throws IOException {
        response.sendRedirect(url);
    }
}
