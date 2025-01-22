package com.enus.newsletter.db.repository;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.SignupRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Log4j2
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

            UserEntity user = UserEntity.builder()
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .build();

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
}
