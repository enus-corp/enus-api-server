package com.enus.newsletter.exception.auth;

import com.enus.newsletter.exception.IErrorCode;

public enum AuthErrorCode implements IErrorCode {
    ACCOUNT_LOCKED(1002, "Account Locked"),
    TOKEN_EXPIRED(1003, "Token Expired"),
    USER_CREATION_FAILED(1004, "User creation failed"),
    ACCOUNT_EXPIRED(1005, "Account expired"),
    ACCOUNT_DISABLED(1006, "Account disabled"),
    INVALID_RESET_PASSWORD_TOKEN(1007, "Invalid reset password token"),
    PASSWORD_ALREADY_USED(1008, "Password already used"),
    USERNAME_ALREADY_EXISTS(1009, "Username already exists"),
    EMAIL_ALREADY_EXISTS(1010, "Email already exists"),
    INVALID_CREDENTIALS(1011, "Invalid credentials"),
    USER_NOT_FOUND(1012, "User not found"),
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
