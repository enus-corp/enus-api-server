package com.enus.newsletter.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.model.dto.UserDTO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final IUserRepository userRepository;

    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userRepository.findByUsername(username)
                .map(UserDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
    
}
