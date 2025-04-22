package com.enus.newsletter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.model.request.auth.ResetPasswordRequest;
import com.enus.newsletter.model.request.auth.SigninRequest;
import com.enus.newsletter.model.request.auth.SignupRequest;
import com.enus.newsletter.model.request.auth.VerifyViaEmailRequest;
import com.enus.newsletter.model.request.keyword.RefreshTokenRequest;
import com.enus.newsletter.model.response.Token;
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
    public ResponseEntity<GeneralServerResponse<Void>> signup(@Valid @RequestBody SignupRequest dto) {
        authService.signup(dto);
        GeneralServerResponse<Void> response = new GeneralServerResponse<>(
            false,
            "Successfully created user",
            200,
            null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<GeneralServerResponse<Token>> signin(HttpServletRequest request, @Valid @RequestBody SigninRequest dto) {
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
    public ResponseEntity<GeneralServerResponse<?>> verifyEmail(@Valid @RequestBody VerifyViaEmailRequest dto) {
        authService.verifyEmail(dto);
        return ResponseEntity.ok(
                new GeneralServerResponse<>(
                        false,
                        "Message sent to email for verification",
                        200,
                        null
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
