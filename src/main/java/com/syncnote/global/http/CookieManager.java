package com.syncnote.global.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieManager {
    public static final String ACCESS_TOKEN_COOKIE = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    public void setAccessToken(
            HttpServletRequest request,
            HttpServletResponse response,
            String token,
            long accessTokenDurationSeconds
    ) {
        set(request, response, ACCESS_TOKEN_COOKIE, token, accessTokenDurationSeconds);
    }

    public void setRefreshToken(
            HttpServletRequest request,
            HttpServletResponse response,
            String token,
            long refreshTokenDurationSeconds
    ) {
        set(request, response, REFRESH_TOKEN_COOKIE, token, refreshTokenDurationSeconds);
    }

    public void deleteAccessToken(HttpServletRequest request, HttpServletResponse response) {
        delete(request, response, ACCESS_TOKEN_COOKIE);
    }

    public void deleteRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        delete(request, response, REFRESH_TOKEN_COOKIE);
    }

    public void deleteAuthCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteAccessToken(request, response);
        deleteRefreshToken(request, response);
    }

    public void set(
            HttpServletRequest request,
            HttpServletResponse response,
            String name,
            String value,
            long maxAgeSeconds
    ) {
        String cookieValue = (value == null) ? "" : value;

        boolean delete = cookieValue.isBlank();
        long maxAge = delete ? 0 : Math.max(maxAgeSeconds, 0);

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, cookieValue)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax") // TODO: need to refactor
                .secure(false) // TODO: need to refactor
                .maxAge(maxAge);

        String domain = ""; // TODO: need to refactor

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        ResponseCookie cookie = builder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void delete(HttpServletRequest request, HttpServletResponse response, String name) {
        set(request, response, name, "", 0);
    }

    private boolean iaHttpsRequest(HttpServletRequest request) {
        String proto = request.getHeader("X-Forwarded-Proto");

        if (proto != null && !proto.isBlank()) {
            return "https".equalsIgnoreCase(proto);
        }

        return request.isSecure();
    }
}
