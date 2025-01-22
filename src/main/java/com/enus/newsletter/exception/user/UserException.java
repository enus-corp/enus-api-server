package com.enus.newsletter.exception.user;

public class UserException extends Exception {
    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserException(UserErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserException(UserErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}
