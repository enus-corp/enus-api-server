/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.exception.auth;

import com.enus.newsletter.exception.IErrorCode;

/**
 *
 * @author idohyeon
 */
public enum TokenErrorCode implements IErrorCode {
    TOKEN_NOT_VALID(0000, "Invalid Token"),
    TOKEN_EXPIRED(0001, "Token Expired"),
    TOKEN_NOT_FOUND(0002, "Token Not Found"),
    TOKEN_ALREADY_USED(0003, "Token Already Used"),
    ;


    private final int code;
    private final String message;

    TokenErrorCode(int code, String message) {
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
