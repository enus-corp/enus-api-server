package com.enus.newsletter.exception.chat;

import com.enus.newsletter.exception.CustomBaseException;

public class ChatException extends CustomBaseException {
    public ChatException(ChatErrorCode errorCode) { super(errorCode); }
    public ChatException(ChatErrorCode errorCode, String message)  { super(errorCode, message); }
    public ChatException(ChatErrorCode errorCode, Throwable cause) { super(errorCode, cause); }
}
