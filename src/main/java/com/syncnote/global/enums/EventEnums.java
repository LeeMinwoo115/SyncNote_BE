package com.syncnote.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventEnums {
    JOIN("JOIN");

    private final String event;
}
