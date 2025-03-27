/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.LoginHistoryRepository;
import com.enus.newsletter.db.repository.PasswordResetTokenRepository;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.auth.ResetPasswordRequest;
import com.enus.newsletter.model.request.auth.SigninRequest;
import com.enus.newsletter.model.request.auth.SignupRequest;
import com.enus.newsletter.model.request.auth.VerifyViaEmailRequest;
import com.enus.newsletter.model.request.keyword.RefreshTokenRequest;
import com.enus.newsletter.model.response.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Random;

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

    public UserEntity signup(SignupRequest dto) throws UserException {
        log.info("Processing signup request for user: {}", dto);

        return userRepository.createUser(dto);
    }

    public Token authenticate(SigninRequest dto, String ip) throws UserException, AuthException {
        log.info("Processing authentication for user: {}", dto);

        // Get user credentials
        Authentication auth = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authResult;

        try {
            // validate credentials using authentication manager
            // Throws exception if authentication fails
            authResult = authenticationManager.authenticate(auth);

        } catch (LockedException e) {
            userRepository.handleLoginAttempt(dto.getUsername(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getUsername(),
                    ip,
                    0,
                    "Authentication failed. Account is locked"
            );

            throw new AuthException(AuthErrorCode.ACCOUNT_LOCKED, "Authentication failed. Account is locked");
        } catch (DisabledException e) {
            userRepository.handleLoginAttempt(dto.getUsername(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getUsername(),
                    ip,
                    0,
                    "Authentication failed. Account is disabled"
            );

            throw new AuthException(AuthErrorCode.ACCOUNT_DISABLED, "Authentication failed. Account is disabled");
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", e.getMessage());
            userRepository.handleLoginAttempt(dto.getUsername(), false);

            loginHistoryRepository.saveLoginHistory(
                    dto.getUsername(),
                    ip,
                    0,
                    "Authentication failed. Invalid username or password"
            );

            throw new AuthException(AuthErrorCode.INVALID_CREDENTIALS, "Authentication failed. Invalid username or password");
        } finally {
            log.info("Authentication attempt failed for user: {}", dto);
        }

        // get authenticated user details from the authentication result
        UserDetails user = (UserDetails) authResult.getPrincipal();

        // reset login attempts
        userRepository.handleLoginAttempt(dto.getUsername(), true);
        // save login history
        loginHistoryRepository.saveLoginHistory(
                dto.getUsername(),
                ip,
                1,
                "Authentication successful"
        );

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return Token
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void verifyEmail(VerifyViaEmailRequest dto) throws UserException, AuthException {
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        passwordResetTokenRepository.verifyEmail(dto.getEmail(), verificationCode);
    }

    public void resetPassword(ResetPasswordRequest dto) throws UserException, AuthException{
        // temporary implementation
        passwordResetTokenRepository.resetPassword(dto);
    }

    public Token refreshToken(RefreshTokenRequest dto) throws UserException, AuthException {
        String username = jwtService.extractUsername(dto.getRefreshToken());
        if (username == null) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, AuthErrorCode.INVALID_TOKEN.getMessage());
        }
        UserEntity user = userRepository.findByUsername(username);
        UserDTO userDTO = new UserDTO(user);

        if (jwtService.isTokenValid(dto.getRefreshToken(), userDTO)) {
            String accessToken = jwtService.generateAccessToken(userDTO);

            return Token
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(dto.getRefreshToken())
                    .build();
        } else {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "Invalid token");
        }
    }

}
