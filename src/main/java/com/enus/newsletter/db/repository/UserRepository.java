package com.enus.newsletter.db.repository;

import com.enus.newsletter.db.AbsBaseRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.auth.SignupRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "UserRepository")
@Repository
@Transactional
public class UserRepository extends AbsBaseRepository<UserEntity, IUserRepository> {

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    public UserRepository(PasswordEncoder passwordEncoder, IUserRepository userRepository) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity getOne(String username) throws UserException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity createUser(SignupRequest dto) throws UserException {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new UserException(UserErrorCode.USER_EXISTS, UserErrorCode.USER_EXISTS.getMessage());
        }
        if (repository.existsByEmail(dto.getEmail())) {
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

            return repository.save(user);

        } catch (Exception e) {
            log.error("Failed to create user: {}", dto.getUsername(), e);
            throw new UserException(UserErrorCode.CREATE_USER_FAILED, e.getMessage());
        }
    }

    public UserEntity verifyEmail(String email) throws UserException {
        UserEntity user = repository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return repository.save(user);
    }

    public UserEntity findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, UserErrorCode.USER_NOT_FOUND.getMessage()));
    }

    public void handleLoginAttempt(String username, boolean isSuccessful) {
        repository.findByUsername(username)
                .ifPresent(user -> {
                    user.handleLoginAttempt(isSuccessful);
                    repository.save(user);
                });
    }
}
