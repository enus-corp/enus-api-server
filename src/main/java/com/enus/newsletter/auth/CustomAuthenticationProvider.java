package com.enus.newsletter.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;
import com.enus.newsletter.interfaces.CustomUserDetailsImpl;
import com.enus.newsletter.service.CustomUserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CUSTOM_AUTHENTICATION_PROVIDER")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(CustomUserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException, AuthException {
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        log.info("Authenticating user with email: {}", email);
        log.info("Password: {}", password);

        // throws UsernameNotFoundException
        CustomUserDetailsImpl userDetailsImpl = (CustomUserDetailsImpl) userDetailsService.loadUserByUsername(email);

        if (passwordEncoder.matches(password, userDetailsImpl.getPassword())) {
            log.info("Password matches for user: {}", email);
            return new EmailPasswordAuthenticationToken(userDetailsImpl, password);
        } else {
            log.error("Invalid credentials for user: {}", email);
            throw new AuthException(AuthErrorCode.INVALID_CREDENTIALS, AuthErrorCode.INVALID_CREDENTIALS.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
}
