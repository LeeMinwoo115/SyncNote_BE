package com.syncnote.domain.user.service;

import com.syncnote.domain.user.dto.response.UserProfileResponse;
import com.syncnote.domain.user.entity.User;
import com.syncnote.domain.user.repository.UserRepository;
import com.syncnote.global.error.code.UserErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserProfileResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(UserErrorCode.NOT_FOUND));

        return UserProfileResponseMapper.from(user);
    }
}
