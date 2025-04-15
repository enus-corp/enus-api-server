/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.service;

import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.enus.newsletter.auth.EmailPasswordAuthenticationToken;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.LoginHistoryRepository;
import com.enus.newsletter.db.repository.PasswordResetTokenRepository;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;
import com.enus.newsletter.exception.auth.TokenErrorCode;
import com.enus.newsletter.exception.auth.TokenException;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.interfaces.CustomUserDetailsImpl;
import com.enus.newsletter.interfaces.ICustomUserDetails;
import com.enus.newsletter.model.request.auth.ResetPasswordRequest;
import com.enus.newsletter.model.request.auth.SigninRequest;
import com.enus.newsletter.model.request.auth.SignupRequest;
import com.enus.newsletter.model.request.auth.VerifyViaEmailRequest;
import com.enus.newsletter.model.request.keyword.RefreshTokenRequest;
import com.enus.newsletter.model.response.Token;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AUTH_SERVICE")
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, LoginHistoryRepository loginHistoryRepository, UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.loginHistoryRepository = loginHistoryRepository;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.jwtService = jwtService;
    }

    public UserEntity signup(SignupRequest dto) throws UserException, TokenException {
        log.info("Processing signup request for user: {}", dto);

        return userRepository.createUser(dto);
    }

    public Token authenticate(SigninRequest dto, String ip) throws UserException, AuthException {
        log.info("Processing authentication for user: {}", dto);

        // Get user credentials
        Authentication auth = new EmailPasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        Authentication authResult;

        try {
            // validate credentials using authentication manager
            // Throws exception if authentication fails
            authResult = authenticationManager.authenticate(auth);

        } catch (LockedException e) {
            userRepository.handleLoginAttempt(dto.getEmail(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getEmail(),
                    ip,
                    0,
                    "Authentication failed. Account is locked"
            );

            throw new AuthException(AuthErrorCode.ACCOUNT_LOCKED, AuthErrorCode.ACCOUNT_LOCKED.getMessage());
        } catch (DisabledException e) {
            log.error("Account is disabled for email: {}", dto.getEmail());
            userRepository.handleLoginAttempt(dto.getEmail(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getEmail(),
                    ip,
                    0,
                    "Authentication failed. Account is disabled"
            );

            throw new AuthException(AuthErrorCode.ACCOUNT_DISABLED, AuthErrorCode.ACCOUNT_DISABLED.getMessage());
        } catch (UsernameNotFoundException e) {
            log.error("User not found for email: {}", dto.getEmail());
            userRepository.handleLoginAttempt(dto.getEmail(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getEmail(),
                    ip,
                    0,
                    "Authentication failed. User not found"
            );

            throw new AuthException(AuthErrorCode.USER_NOT_FOUND, AuthErrorCode.USER_NOT_FOUND.getMessage());
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", e.getMessage());
            userRepository.handleLoginAttempt(dto.getEmail(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getEmail(),
                    ip,
                    0,
                    "Authentication failed. Invalid username or password"
            );

            throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, TokenErrorCode.TOKEN_NOT_VALID.getMessage());
        }

        // get authenticated user details from the authentication result
        ICustomUserDetails user = (ICustomUserDetails) authResult.getPrincipal();

        // reset login attempts
        userRepository.handleLoginAttempt(dto.getEmail(), true);
        // save login history
        loginHistoryRepository.saveLoginHistory(
                dto.getEmail(),
                ip,
                1,
                "Authentication successful"
        );

        return generateToken(user);
    }

    public void verifyEmail(VerifyViaEmailRequest dto) throws UserException, AuthException {
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        passwordResetTokenRepository.verifyEmail(dto.getEmail(), verificationCode);
    }

    public void resetPassword(ResetPasswordRequest dto) throws UserException, AuthException{
        // temporary implementation
        passwordResetTokenRepository.resetPassword(dto);
    }

    public Token refreshToken(RefreshTokenRequest dto) throws UserException, AuthException, TokenException {
        String email = jwtService.extractEmail(dto.getRefreshToken());
        if (email == null) {
            throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, TokenErrorCode.TOKEN_NOT_VALID.getMessage());
        }
        UserEntity user = userRepository.findByEmail(email);
        ICustomUserDetails userDetails = new CustomUserDetailsImpl(user);

        if (jwtService.isTokenValid(dto.getRefreshToken(), userDetails)) {
            String accessToken = jwtService.generateAccessToken(userDetails);

            return Token
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(dto.getRefreshToken())
                    .build();
        } else {
            throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, TokenErrorCode.TOKEN_NOT_VALID.getMessage());
        }
    }

    public void createUserByEmail(String email) throws AuthException {
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
        }
        userRepository.createUserByEmail(email);
        
    }

    private Token generateToken(ICustomUserDetails userDetails) {
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return Token
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
