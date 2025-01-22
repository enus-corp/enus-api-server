/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.PasswordResetTokenRepository;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.ResetPasswordRequest;
import com.enus.newsletter.model.request.SigninRequest;
import com.enus.newsletter.model.request.SignupRequest;
import com.enus.newsletter.model.request.VerifyViaEmailRequest;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.model.response.VerifyViaEmail;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@Transactional
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.jwtService = jwtService;
    }

    public UserEntity signup(SignupRequest dto) throws UserException {
        log.info("Processing signup request for user: {}", dto);

        return userRepository.createUser(dto);
    }

    public Token authenticate(SigninRequest dto) throws UserException {
        log.info("Processing authentication for user: {}", dto);

        // create authentication token with username and password
        Authentication auth = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        try {
            // validate credentials using authentication manager
            Authentication authResult = authenticationManager.authenticate(auth);

            // get authenticated user details from the authentication result
            UserDetails u = (UserDetails) authResult.getPrincipal();

            UserEntity user = userRepository.getOne(u.getUsername());

            String jwtToken = jwtService.generateToken(user);

            return Token
                    .builder()
                    .token(jwtToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public VerifyViaEmail verifyEmail(VerifyViaEmailRequest dto) throws Exception {
        UserEntity user = userRepository.verifyEmail(dto.getEmail());

        String responseCode = generateVerificationCode();
        String emailCode = generateVerificationCode();
        return VerifyViaEmail
                .builder()
                .otp(responseCode)
                .build();
    }

    public void resetPassword(ResetPasswordRequest dto) throws UserException{
        // temporary implementation
        passwordResetTokenRepository.resetPassword(dto);

    }



    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
