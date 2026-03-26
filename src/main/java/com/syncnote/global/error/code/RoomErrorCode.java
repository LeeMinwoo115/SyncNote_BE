package com.syncnote.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RoomErrorCode implements ErrorCode{
    NOT_FOUND(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청값을 다시 확인해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
