package com.enus.newsletter.interfaces;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.enus.newsletter.db.entity.UserEntity;

public class CustomUserDetailsImpl implements ICustomUserDetails{
    private final UserEntity user;

    public CustomUserDetailsImpl(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    
}
