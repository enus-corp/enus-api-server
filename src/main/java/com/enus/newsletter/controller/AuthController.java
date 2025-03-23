package com.enus.newsletter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.model.request.auth.ResetPasswordRequest;
import com.enus.newsletter.model.request.auth.SigninRequest;
import com.enus.newsletter.model.request.auth.SignupRequest;
import com.enus.newsletter.model.request.auth.VerifyViaEmailRequest;
import com.enus.newsletter.model.request.keyword.RefreshTokenRequest;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.model.response.VerifyViaEmail;
import com.enus.newsletter.service.AuthService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name="Auth", description = "Authentication")
@Slf4j(topic = "AUTH_CONTROLLER")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<GeneralServerResponse<UserEntity>> signup(@Valid @RequestBody SignupRequest dto) {
        log.info("---------------- Signup request: {} ----------------", dto);
        UserEntity user = authService.signup(dto);
        GeneralServerResponse<UserEntity> response = new GeneralServerResponse<>(
            false,
            "Successfully created user",
            200,
            user
        );

        log.info("---------------- Signup response: {} ----------------", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<GeneralServerResponse<Token>> signin(HttpServletRequest request, @Valid @RequestBody SigninRequest dto) {
        log.info("\t >>> Signin Request");
        String ip = request.getHeader("X-FORWARDED-FOR");
        ip = ip != null ? ip.split(",")[0] : request.getRemoteAddr();
        Token token = authService.authenticate(dto, ip);
        GeneralServerResponse<Token> response = new GeneralServerResponse<>(
                false,
                "Successfully authenticated user",
                200,
                token
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<GeneralServerResponse<VerifyViaEmail>> verifyEmail(@Valid @RequestBody VerifyViaEmailRequest dto) {
        VerifyViaEmail verification = authService.verifyEmail(dto);
        return ResponseEntity.ok(
                new GeneralServerResponse<>(
                        false,
                        "Successfully verified email",
                        200,
                        verification
                )
        );
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<GeneralServerResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok(
                new GeneralServerResponse<>(
                        false,
                        "Successfully reset password",
                        200,
                        null
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String entity) {
        return ResponseEntity.ok("logout");
    }

    @PostMapping("/refresh")
    public ResponseEntity<GeneralServerResponse<Token>> refresh(@Valid @RequestBody RefreshTokenRequest dto) {
        Token token = authService.refreshToken(dto);
        GeneralServerResponse<Token> response = new GeneralServerResponse<>(
                false,
                "Successfully refreshed new token",
                200,
                token
        );
        return ResponseEntity.ok(response);
    }


}
