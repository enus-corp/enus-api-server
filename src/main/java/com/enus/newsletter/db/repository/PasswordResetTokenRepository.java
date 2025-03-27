package com.enus.newsletter.db.repository;

import com.enus.newsletter.db.AbsBaseRepository;
import com.enus.newsletter.db.entity.OldPasswordHistoryEntity;
import com.enus.newsletter.db.entity.PasswordResetTokenEntity;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IOldPasswordHistoryRepository;
import com.enus.newsletter.db.repository.imp.IPasswordResetTokenRepository;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;
import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.auth.ResetPasswordRequest;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Log4j2
@Repository
@Transactional
public class PasswordResetTokenRepository extends AbsBaseRepository<PasswordResetTokenEntity, IPasswordResetTokenRepository> {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IOldPasswordHistoryRepository oldPasswordHistoryRepository;

    public PasswordResetTokenRepository(PasswordEncoder passwordEncoder, IUserRepository userRepository, IPasswordResetTokenRepository passwordResetTokenRepository, IOldPasswordHistoryRepository oldPasswordHistoryRepository) {
        super(passwordResetTokenRepository);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.oldPasswordHistoryRepository = oldPasswordHistoryRepository;
    }

    public void verifyEmail(String email, String resetPasswordCode) throws UserException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        
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

    public void resetPassword(ResetPasswordRequest dto) throws UserException {
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
        if(oldPasswordHistoryRepository.existsByUserIdAndEncodedPassword(user.getId(), newEncodedPassword)) {
            throw new AuthException(AuthErrorCode.PASSWORD_ALREADY_USED, AuthErrorCode.PASSWORD_ALREADY_USED.getMessage());
        }
        OldPasswordHistoryEntity oldPasswordHistoryEntity = new OldPasswordHistoryEntity(user.getId(), newEncodedPassword);
        // update password in user entity
        user.resetPassword(newEncodedPassword);

        // Add new password to password history
        oldPasswordHistoryRepository.save(oldPasswordHistoryEntity);
        // save/update user password
        userRepository.save(user);
        // delete verification token
        repository.delete(resetToken);
    }
}
