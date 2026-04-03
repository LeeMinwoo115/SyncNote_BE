package com.syncnote.domain.auth.controller;

import com.syncnote.domain.auth.dto.request.EmailRequest;
import com.syncnote.domain.auth.service.EmailVerificationService;
import com.syncnote.global.http.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/email-verification")
@RequiredArgsConstructor
@Validated
@Tag(name = "Email API", description = "이메일 인증 API")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/request")
    public ApiResponse<Void> request(@RequestBody EmailRequest request) {
        emailVerificationService.requestVerification(request.email());
        return ApiResponse.noContent("인증 메일을 발송했습니다.");
    }

    @GetMapping("/confirm")
    public ApiResponse<Void> confirm(@RequestParam String token) {
        emailVerificationService.confirmVerification(token);
        return ApiResponse.noContent("이메일 인증이 완료되었습니다.");
    }
}
