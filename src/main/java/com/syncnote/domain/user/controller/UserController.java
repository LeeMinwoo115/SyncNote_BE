package com.syncnote.domain.user.controller;

import com.syncnote.domain.user.dto.response.UserProfileResponse;
import com.syncnote.domain.user.service.UserService;
import com.syncnote.global.http.ApiResponse;
import com.syncnote.global.http.HttpRequestContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User API", description = "사용자 정보 조회 및 수정, 회원 탈퇴 API")
public class UserController {

    private final UserService userService;
    private final HttpRequestContext httpRequestContext;

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getProfile() {
        long userId = httpRequestContext.getUserId();
        UserProfileResponse response = userService.getUser(userId);
        return ApiResponse.ok(String.format("%s 사용자 조회 성공", userId), response);
    }
}
