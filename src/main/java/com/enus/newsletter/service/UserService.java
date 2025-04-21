package com.enus.newsletter.service;

import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.user.UpdateUserRequest;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

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
            .gender(userEntity.getGender())
            .age(userEntity.getAge())
            .isOauthUser(userEntity.isOauthUser())
            .build();
    }

    public void updateUser(String email, UpdateUserRequest request) {
        UserEntity user = userRepository.findByEmail(email);
        
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        if (!request.getUsername().equals(user.getUsername())) {
            user.setUsername(request.getUsername());
        } else {
            throw new AuthException(
                AuthErrorCode.USERNAME_ALREADY_EXISTS, 
                AuthErrorCode.USERNAME_ALREADY_EXISTS.getMessage()
            );
        }

        user.setGender(Gender.valueOf(request.getGender()));
        user.setAge(request.getAge());

        userRepository.save(user);
    }

}
