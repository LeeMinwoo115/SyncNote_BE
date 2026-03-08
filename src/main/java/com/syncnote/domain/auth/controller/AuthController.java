package com.syncnote.domain.auth.controller;

import com.syncnote.domain.auth.dto.request.LoginRequest;
import com.syncnote.domain.auth.dto.request.SignupRequest;
import com.syncnote.domain.auth.dto.response.AuthResponse;
import com.syncnote.domain.auth.service.AuthService;
import com.syncnote.global.enums.UserRole;
import com.syncnote.global.http.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth API", description = "사용자 회원가입, 로그인")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "사용자 회원가입", description = "이메일, 닉네임, 비밀번호, 생년월일로 회원가입")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request, UserRole.NORMAL);
        return ApiResponse.created("회원가입 성공", response);
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인", description = "이메일, 비밀번호로 로그인")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);
        return ApiResponse.created("로그인 성공", response);
    }
}
