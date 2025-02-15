package com.enus.newsletter.model.dto;

import com.enus.newsletter.db.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDTO implements UserDetails {
    private final String username;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDTO(UserEntity user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.authorities = user.getHasRole().stream()
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
