package com.enus.newsletter.db.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.AbsBaseRepository;
import com.enus.newsletter.db.entity.PasswordHistoryEntity;
import com.enus.newsletter.db.entity.PasswordResetTokenEntity;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IPasswordHistoryRepository;
import com.enus.newsletter.db.repository.imp.IPasswordResetTokenRepository;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;
import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.auth.ResetPasswordRequest;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
@Transactional
public class PasswordResetTokenRepository extends AbsBaseRepository<PasswordResetTokenEntity, IPasswordResetTokenRepository> {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IPasswordHistoryRepository passwordHistoryRepository;

    public PasswordResetTokenRepository(PasswordEncoder passwordEncoder, IUserRepository userRepository, IPasswordResetTokenRepository passwordResetTokenRepository, IPasswordHistoryRepository passwordHistoryRepository) {
        super(passwordResetTokenRepository);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    public void verifyEmail(String email, String resetPasswordCode) throws UserException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        log.info("oauth user status -> {}", user.isOauthUser());
        
        if (user.isOauthUser()) {
            throw new UserException(UserErrorCode.RESTRICTED_ACTION, "User is not allowed to change password");

        }
        // search for existing password reset tokens. invalidate prevoius tokens if exists

        PasswordResetTokenEntity otcEntity = new PasswordResetTokenEntity(
            user,
            resetPasswordCode,
            LocalDateTime.now().plusMinutes(5)
        );
        repository.save(otcEntity);
        // TODO send password code
    }

    private void sendEmail(String email, String resetPasswordCode) {
        String from = "";
        String to = email;
        String cc = "";
        String bcc = "";
        String subject = "Reset Password Code";

        // send email
    }

    public void resetPassword(ResetPasswordRequest dto) throws AuthException {
        // get record from password reset token
        PasswordResetTokenEntity resetToken = repository.findValidTokenByEmailCode(
                dto.getVerificationCode(),
                LocalDateTime.now()
        )
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_RESET_PASSWORD_TOKEN, AuthErrorCode.INVALID_RESET_PASSWORD_TOKEN.getMessage()));

        // get user from token
        UserEntity user = resetToken.getUser();
        if(user.getIsLocked() == 1) {
            throw new AuthException(AuthErrorCode.ACCOUNT_LOCKED, AuthErrorCode.ACCOUNT_LOCKED.getMessage());
        }
        // encode new password
        String newEncodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // check if new password is same as old password
        if(passwordHistoryRepository.existsByUserIdAndPassword(user.getId(), newEncodedPassword)) {
            throw new AuthException(AuthErrorCode.PASSWORD_ALREADY_USED, AuthErrorCode.PASSWORD_ALREADY_USED.getMessage());
        }
        PasswordHistoryEntity passwordHistoryEntity = new PasswordHistoryEntity(user, newEncodedPassword);
        // update password in user entity
        user.resetPassword(newEncodedPassword);

        // Add new password to password history
        passwordHistoryRepository.save(passwordHistoryEntity);
        // save/update user password
        userRepository.save(user);
        // delete verification token
        repository.delete(resetToken);
    }
}
