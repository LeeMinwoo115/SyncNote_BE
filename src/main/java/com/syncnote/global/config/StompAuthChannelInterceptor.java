package com.syncnote.global.config;

import com.syncnote.global.utils.JwtClaims;
import com.syncnote.global.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);
        var command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            String auth = accessor.getFirstNativeHeader("Authorization");
            Long userId = verifyJwtAndGetUserId(auth);

            if (userId != null) {
                var authToken = new UsernamePasswordAuthenticationToken(userId, null, List.of());
                accessor.setUser(authToken);
            }

            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
        }

        return message;
    }

    private Long verifyJwtAndGetUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7).trim();

        try {
            JwtClaims claims = jwtProvider.payloadOrNull(token);

            if (claims == null) {
                return null;
            }

            return claims.userId();
        } catch (Exception e) {
            return null;
        }
    }
}
