package com.enus.newsletter.exception.keyword;

import com.enus.newsletter.exception.IErrorCode;

public enum KeywordErrorCode implements IErrorCode {
    KEYWORD_NOT_FOUND(2000, "Keyword not found"),
    KEYWORD_ALREADY_EXISTS(2001, "Keyword already exists"),
    KEYWORD_INVALID(2002, "Keyword is invalid"),
    KEYWORD_NOTIFICATION_DISABLED(2003, "Keyword notification is disabled");

    private final int code;
    private final String message;

    KeywordErrorCode(int code, String message) {
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
