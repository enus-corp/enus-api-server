/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.exception.auth;

import com.enus.newsletter.exception.CustomBaseException;

/**
 *
 * @author idohyeon
 */
public class TokenException extends CustomBaseException {

    public TokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(TokenErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TokenException(TokenErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
