package com.syncnote.domain.user.service;

import com.syncnote.domain.user.dto.response.UserProfileResponse;
import com.syncnote.domain.user.entity.User;

import java.time.format.DateTimeFormatter;

public class UserProfileResponseMapper {

    private static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static UserProfileResponse from(User user) {

        String signupDateStr = user.getCreatedAt() != null
                ? user.getCreatedAt().toLocalDate().format(DATE_FORMATTER)
                : null;

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                signupDateStr
        );
    }
}
