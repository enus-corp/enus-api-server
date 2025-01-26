package com.enus.newsletter.controller;


import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.model.request.ResetPasswordRequest;
import com.enus.newsletter.model.request.VerifyViaEmailRequest;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.model.response.VerifyViaEmail;
import com.enus.newsletter.service.AuthService;
import com.enus.newsletter.model.request.SigninRequest;
import com.enus.newsletter.model.request.SignupRequest;
import com.enus.newsletter.system.GeneralServerResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Auth", description = "Authentication")
@Slf4j(topic = "AuthController")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    public ResponseEntity<GeneralServerResponse<UserEntity>> signup(@Valid @RequestBody SignupRequest dto) {
      log.info("---------------- Signup request: {} ----------------", dto);
      UserEntity user = authService.signup(dto);
      GeneralServerResponse<UserEntity> response = new GeneralServerResponse<UserEntity>(
            false,
            "Successfully created user",
            200,
            user
      );

      log.info("---------------- Signup response: {} ----------------", response);
      return ResponseEntity.ok(response);

    }

    @PostMapping("/signin")
    public ResponseEntity<GeneralServerResponse<Token>> signin(@RequestBody SigninRequest dto) {
        Token token = authService.authenticate(dto);
        GeneralServerResponse<Token> response = new GeneralServerResponse<Token>(
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
                new GeneralServerResponse<Void>(
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
    public ResponseEntity<GeneralServerResponse<Token>> refresh(@RequestBody String refreshToken) {
        Token token = authService.refreshToken(refreshToken);
        GeneralServerResponse<Token> response = new GeneralServerResponse<Token>(
                false,
                "Successfully refreshed new token",
                200,
                token
        );
        return ResponseEntity.ok(response);
    }


}
