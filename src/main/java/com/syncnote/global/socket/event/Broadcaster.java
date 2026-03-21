package com.syncnote.global.socket.event;

public interface Broadcaster<T> {
    void onCreated(T event);
}
