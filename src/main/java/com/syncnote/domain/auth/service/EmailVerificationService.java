package com.syncnote.domain.auth.service;

import com.syncnote.domain.auth.entity.EmailVerification;
import com.syncnote.domain.auth.repository.EmailVerificationRepository;
import com.syncnote.global.error.code.AuthErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import com.syncnote.global.resolver.FrontendUrlResolver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    private final FrontendUrlResolver frontendUrlResolver;

    @Transactional
    public void requestVerification(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .map(existing -> {
                    existing.refreshToken(token, expiresAt);
                    return existing;
                })
                .orElse(
                        EmailVerification.builder()
                                .email(email)
                                .token(token)
                                .verified(false)
                                .expiresAt(expiresAt)
                                .build()
                );

        emailVerificationRepository.save(emailVerification);

        String frontendBaseUrl = frontendUrlResolver.resolveBaseUrl();
        String verifyUrl = frontendBaseUrl + "/auth/email-verification?token=" + token;
        emailService.sendVerificationEmail(email, verifyUrl);
    }

    @Transactional
    public void confirmVerification(String token) {
        EmailVerification verification = emailVerificationRepository.findByToken(token)
                .orElseThrow(() -> new ErrorException(AuthErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN));

        if (verification.isExpired()) {
            throw new ErrorException(AuthErrorCode.EMAIL_VERIFICATION_TOKEN_EXPIRED);
        }

        verification.verify();
    }

    @Transactional
    public void validateVerifiedEmail(String email) {
        EmailVerification verification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(AuthErrorCode.EMAIL_NOT_VERIFIED));

        if (!verification.isVerified()) {
            throw new ErrorException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }
    }
}
