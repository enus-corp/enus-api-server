package com.enus.newsletter.model.dto;

import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.interfaces.ICustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

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
    private final boolean isOauthUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
