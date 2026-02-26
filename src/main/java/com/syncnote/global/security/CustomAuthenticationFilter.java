package com.syncnote.global.security;

import com.syncnote.domain.auth.dto.JwtDto;
import com.syncnote.domain.auth.service.AuthTokenService;
import com.syncnote.global.enums.UserRole;
import com.syncnote.global.error.code.AuthErrorCode;
import com.syncnote.global.error.code.ErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import com.syncnote.global.http.ApiResponse;
import com.syncnote.global.http.CookieManager;
import com.syncnote.global.utils.JwtClaims;
import com.syncnote.global.utils.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private static final Set<String> AUTH_EXACT_WHITELIST = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/api/v1/auth/logout",
            "/api/v1/test"
    );

    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;
    private final ObjectMapper objectMapper;
    private final AuthTokenService authTokenService;

    @Value("${custom.jwt.access-token-duration:3600}")
    private long accessTokenDurationSeconds;

    @Value("${custom.jwt.refresh-token-duration:1209600}")
    private long refreshTokenDurationSeconds;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException {
        try {
            authenticate(request, response, filterChain);
        } catch (ErrorException e) {
            handleErrorException(request, response, e);
        } catch (Exception e) {
            log.error("Auth Filter Error: ", e);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void authenticate(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestUrl = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
            || !requestUrl.startsWith("/api/")
            || AUTH_EXACT_WHITELIST.contains(requestUrl)
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(request);

        String validAccessToken = ensureValidAccessToken(accessToken, request, response);

        JwtClaims claims = jwtProvider.payloadOrNull(validAccessToken);
        if (claims == null || !"access".equals(claims.tokenType())) {
            throw new ErrorException(AuthErrorCode.INVALID_TOKEN);
        }

        long userId = claims.userId();
        String nickname = claims.nickname();
        UserRole role = claims.role();

        SecurityUser securityUser = new SecurityUser(
                userId,
                nickname,
                "",
                role,
                List.of(new SimpleGrantedAuthority(role.toAuthority()))
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                securityUser.getPassword(),
                securityUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String headerAuthorization = request.getHeader("Authorization");
        if (!StringUtils.isBlank(headerAuthorization)
                && headerAuthorization.startsWith(BEARER_PREFIX)
        ) {
            String value = headerAuthorization.substring(BEARER_PREFIX.length());
            return StringUtils.isBlank(value) ? null : value;
        }
        return resolveCookie(request, "accessToken");
    }

    private String resolveCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                String value = cookie.getValue();
                return StringUtils.isBlank(value) ? null : value;
            }
        }

        return null;
    }

    private String ensureValidAccessToken(
            String accessToken,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(accessToken)) {
            throw new ErrorException(AuthErrorCode.UNAUTHORIZED);
        }

        if (!jwtProvider.isExpired(accessToken)) {
            return accessToken;
        }

        log.info("Expired Access Token → Try to Issue new Access Token, expired access token: {}",
                accessToken);
        String refreshTokenStr = resolveCookie(request, "refreshToken");

        if (StringUtils.isBlank(refreshTokenStr) || jwtProvider.isExpired(refreshTokenStr)) {
            throw new ErrorException(AuthErrorCode.TOKEN_EXPIRED);
        }

        JwtDto newTokens = authTokenService.updateTokens(refreshTokenStr);
        cookieManager.set(request, response, "accessToken", newTokens.accessToken(), accessTokenDurationSeconds);
        cookieManager.set(request, response, "refreshToken", newTokens.refreshToken(), refreshTokenDurationSeconds);

        return newTokens.accessToken();
    }

    private void handleErrorException(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorException error
    ) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        if (error.getErrorCode() == AuthErrorCode.ACCESS_OTHER_DEVICE
                || error.getErrorCode() == AuthErrorCode.REFRESH_TOKEN_NOT_FOUND
                || error.getErrorCode() == AuthErrorCode.TOKEN_EXPIRED
        ) {
            cookieManager.deleteAuthCookies(request, response);
        }

        log.error("CustomAuthenticationFilter: ", error);
        writeError(response, error.getErrorCode());
    }

    private void writeError(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<?> body = ApiResponse.fail(code);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
