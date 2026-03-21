package com.syncnote.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages
    ) {
        messages
                // CONNECT, DISCONNECT 등 destination 없는 메시지
                .nullDestMatcher().authenticated()

                // 에러 큐는 허용
                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()

                // 클라이언트 -> 서버 전송
                .simpDestMatchers("/app/**").authenticated()

                // 서버 -> 클라이언트 구독
                .simpSubscribeDestMatchers("/topic/**", "/user/**").authenticated()

                // 그 외 MESSAGE/SUBSCRIBE는 차단
                .simpTypeMatchers(
                        org.springframework.messaging.simp.SimpMessageType.MESSAGE,
                        org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE
                ).denyAll()

                .anyMessage().denyAll();

        return messages.build();
    }
}
