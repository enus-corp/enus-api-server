package com.enus.newsletter.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.interfaces.CustomUserDetailsImpl;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{

    private final IUserRepository userRepository;

    public CustomUserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO. Should use use user not found exception
    @Override
    public CustomUserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetailsImpl(user);
    }
}
