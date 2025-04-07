package com.enus.newsletter.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.model.dto.UserDTO;


@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return UserDTO.builder()
            .userId(userEntity.getId())
            .firstName(userEntity.getFirstName())
            .lastName(userEntity.getLastName())
            .username(userEntity.getUsername())
            .email(userEntity.getEmail())
            .isOauthUser(userEntity.isOauthUser())
            .build();
    }

}
