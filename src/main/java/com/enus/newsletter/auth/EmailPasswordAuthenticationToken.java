package com.enus.newsletter.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class EmailPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken{
    public EmailPasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    @Override
    public Object getCredentials() {
        return super.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return super.getPrincipal();
    }
    
}
