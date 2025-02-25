package com.enus.newsletter.db.repository;

import com.enus.newsletter.db.AbsBaseRepository;
import com.enus.newsletter.db.entity.PasswordResetToken;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IPasswordResetTokenRepository;
import com.enus.newsletter.db.repository.imp.IUserRepository;
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
public class PasswordResetTokenRepository extends AbsBaseRepository<PasswordResetToken, IPasswordResetTokenRepository> {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;

    public PasswordResetTokenRepository(PasswordEncoder passwordEncoder, IUserRepository userRepository, IPasswordResetTokenRepository passwordResetTokenRepository) {
        super(passwordResetTokenRepository);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void resetPassword(ResetPasswordRequest dto) throws UserException {
        PasswordResetToken resetToken = repository.findValidTokenByEmailCode(
                dto.getVerificationCode(),
                dto.getEmailCode(),
                LocalDateTime.now()
        )
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_RESET_PASSWORD_TOKEN));

        UserEntity user = resetToken.getUser();
        user.resetPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        repository.delete(resetToken);
    }
}
