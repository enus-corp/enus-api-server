package com.enus.newsletter.exception.auth;

import com.enus.newsletter.exception.IErrorCode;

public enum AuthErrorCode implements IErrorCode {
    INVALID_CREDENTIALS(1001, "Invalid Credentials"),
    ACCOUNT_LOCKED(1002, "Account Locked"),
    TOKEN_EXPIRED(1003, "Token Expired"),
    USER_CREATION_FAILED(1004, "User creation failed")
    ;


    private final int code;
    private final String message;

    AuthErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
