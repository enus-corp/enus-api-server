package com.enus.newsletter.db.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.SignupRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "UserRepository")
@Repository
@Transactional
public class UserRepository{

    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    public UserRepository(PasswordEncoder passwordEncoder, IUserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity getOne(String username) throws UserException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity createUser(SignupRequest dto) throws UserException {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserException(UserErrorCode.USER_EXISTS, UserErrorCode.USER_EXISTS.getMessage());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserException(UserErrorCode.USER_EXISTS, UserErrorCode.USER_EXISTS.getMessage());
        }
        try {
            log.info("Creating new user: {}", dto);

            UserEntity user = new UserEntity(
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getUsername(),
                    passwordEncoder.encode(dto.getPassword()),
                    dto.getEmail()
            );

            return userRepository.save(user);

        } catch (Exception e) {
            log.error("Failed to create user: {}", dto.getUsername(), e);
            throw new UserException(UserErrorCode.CREATE_USER_FAILED, e.getMessage());
        }
    }

    public UserEntity verifyEmail(String email) throws UserException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return userRepository.save(user);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, UserErrorCode.USER_NOT_FOUND.getMessage()));
    }

    public void handleLoginAttempt(String username, boolean isSuccessful) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.handleLoginAttempt(isSuccessful);
                    userRepository.save(user);
                });
    }
}
