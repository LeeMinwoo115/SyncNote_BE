package com.syncnote.domain.room.chat.controller;

import com.syncnote.domain.room.chat.dto.request.ReadChatMessageRequest;
import com.syncnote.domain.room.chat.dto.request.SendChatMessageRequest;
import com.syncnote.domain.room.chat.dto.response.ChatMessageResponse;
import com.syncnote.domain.room.chat.service.ChatService;
import com.syncnote.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
@Tag(name = "Chat 웹 소켓", description = "사용자 채팅 소켓")
public class ChatSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void sendMessage(
            @AuthenticationPrincipal SecurityUser securityUser,
            SendChatMessageRequest request
    ) {
        ChatMessageResponse response = chatService.sendMessage(securityUser.getId(), request);
        messagingTemplate.convertAndSend(
                "/topic/rooms/" + request.roomId() + "/messages",
                response
        );
    }

    @MessageMapping("/chat/read")
    public void readMessage(
            @AuthenticationPrincipal SecurityUser securityUser,
            ReadChatMessageRequest request
    ) {
        chatService.updateLastRead(securityUser.getId(), request);

        messagingTemplate.convertAndSend(
                "/topic/rooms/" + request.roomId() + "/read",
                request
        );
    }
}
