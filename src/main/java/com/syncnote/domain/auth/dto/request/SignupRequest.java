package com.syncnote.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 회원가입 요청 DTO")
public record SignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 100)
        @Schema(
                description = "이메일",
                example = "test@test.com"
        )
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호는 최소 8 글자여야 합니다.")
        @Schema(
                description = "비밀번호",
                example = "abc12345"
        )
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2~10 글자여야 합니다.")
        String nickname
) {
}
