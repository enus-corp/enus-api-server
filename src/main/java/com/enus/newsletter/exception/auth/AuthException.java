package com.enus.newsletter.exception.auth;

import com.enus.newsletter.exception.CustomBaseException;

public class AuthException extends CustomBaseException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(AuthErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
