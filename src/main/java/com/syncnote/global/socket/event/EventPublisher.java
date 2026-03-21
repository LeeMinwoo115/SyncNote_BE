package com.syncnote.global.socket.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher publisher;

    public <T> void publishEvent(T event) {
        publisher.publishEvent(event);
    }
}
