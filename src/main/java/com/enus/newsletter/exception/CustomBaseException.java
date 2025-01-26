package com.enus.newsletter.exception;

import lombok.Getter;

@Getter
public abstract class CustomBaseException extends RuntimeException{
    private final IErrorCode errorCode;

    protected CustomBaseException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected CustomBaseException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected CustomBaseException(IErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode.getCode();
    }

}
