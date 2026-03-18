package com.syncnote.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NoteErrorCode implements ErrorCode{
    NOT_FOUND(HttpStatus.NOT_FOUND, "노트를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
