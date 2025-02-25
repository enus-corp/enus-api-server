package com.enus.newsletter.model.dto;

import com.enus.newsletter.db.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserDTO implements UserDetails {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String email;
//    private final Collection<? extends GrantedAuthority> authorities;

    public UserDTO(UserEntity user) {
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();

//        this.authorities = user.getHasRole().stream()
//                .map(role -> (GrantedAuthority) () -> role)
//                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
