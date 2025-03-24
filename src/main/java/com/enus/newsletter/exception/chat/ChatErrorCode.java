package com.enus.newsletter.exception.chat;

import com.enus.newsletter.exception.IErrorCode;

public enum ChatErrorCode implements IErrorCode {
    CHATID_NOT_FOUND(3000, "Chat id not found")
    ;

    private final int code;
    private final String message;

    ChatErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
