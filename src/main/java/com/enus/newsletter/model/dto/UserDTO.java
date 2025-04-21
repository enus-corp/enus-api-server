package com.enus.newsletter.model.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.interfaces.ICustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDTO implements ICustomUserDetails {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String email;
    private final Gender gender;
    private final int age;
    private final boolean isOauthUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
