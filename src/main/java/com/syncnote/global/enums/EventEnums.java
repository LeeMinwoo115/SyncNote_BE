package com.syncnote.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventEnums {
    ROOM("ROOM");

    private final String event;
}
