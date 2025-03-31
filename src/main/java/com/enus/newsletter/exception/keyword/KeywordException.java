package com.enus.newsletter.exception.keyword;

import com.enus.newsletter.exception.CustomBaseException;

public class KeywordException extends CustomBaseException {
    public KeywordException(KeywordErrorCode errorCode) { super(errorCode); }
    public KeywordException(KeywordErrorCode errorCode, String message)  { super(errorCode, message); }
    public KeywordException(KeywordErrorCode errorCode, Throwable cause) { super(errorCode, cause); }
}
