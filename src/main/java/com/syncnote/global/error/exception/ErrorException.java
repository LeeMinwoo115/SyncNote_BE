package com.syncnote.global.error.exception;

import com.syncnote.global.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " - " + message);
        this.errorCode = errorCode;
    }
}
