package com.enus.newsletter.exception.user;

import com.enus.newsletter.exception.CustomBaseException;

public class UserException extends CustomBaseException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(UserErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UserException(UserErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
