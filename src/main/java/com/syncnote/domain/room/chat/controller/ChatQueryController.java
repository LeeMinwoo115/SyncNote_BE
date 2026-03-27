package com.syncnote.domain.room.chat.controller;

import com.syncnote.domain.room.chat.dto.response.GetRoomMessagesResponse;
import com.syncnote.domain.room.chat.dto.response.GetUnreadChatCountResponse;
import com.syncnote.domain.room.chat.service.ChatService;
import com.syncnote.global.http.ApiResponse;
import com.syncnote.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/messages")
@RequiredArgsConstructor
@Validated
@Tag(name = "Chat API", description = "사용자 채팅 API")
public class ChatQueryController {

    private final ChatService chatService;

    @GetMapping
    public ApiResponse<GetRoomMessagesResponse> getMessages(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long roomId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "30") int size
    ) {
        long userId = user.getId();
        GetRoomMessagesResponse response = chatService.getMessages(userId, roomId, cursor, size);
        return ApiResponse.ok(response);
    }

    @GetMapping("/unread-count")
    public ApiResponse<GetUnreadChatCountResponse> getUnreadCount(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long roomId
    ) {
        GetUnreadChatCountResponse response = chatService.getUnreadCount(user.getId(), roomId);
        return ApiResponse.ok(response);
    }
}
