package com.enus.newsletter.exception.user;

import com.enus.newsletter.exception.IErrorCode;

public enum UserErrorCode implements IErrorCode {
    USER_NOT_FOUND(2001, "User Not Found"),
    INVALID_USER_DATA(2002, "Invalid User Data"),
    USER_EXISTS(2003, "User Already Exists"),
    CREATE_USER_FAILED(2004, "Create User Failed"),
    RESTRICTED_ACTION(2005, "Restricted Action")
    ;

    private final int code;
    private final String message;

    UserErrorCode(int code, String message) {
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
