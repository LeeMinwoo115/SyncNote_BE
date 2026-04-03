package com.syncnote.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationEmail(String to, String verifyUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("[SyncNote] 이메일 인증");
        message.setText("""
                안녕하세요.
                아래 링크를 클릭하면 이메일 인증이 완료됩니다.
                %s
                링크 만료 후에는 다시 인증 요청을 해주세요.
                """.formatted(verifyUrl));

        javaMailSender.send(message);
    }
}
