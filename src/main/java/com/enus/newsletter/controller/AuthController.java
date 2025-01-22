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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="Auth", description = "Authentication")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    public ResponseEntity<GeneralServerResponse<UserEntity>> signup(@Valid @RequestBody SignupRequest dto) {
      log.info(">>>>>>>>> Signup request: {}", dto);
        try {
            UserEntity user = authService.signup(dto);
            GeneralServerResponse<UserEntity> response = new GeneralServerResponse<UserEntity>(
                    false,
                    "Successfully created user",
                    200,
                    user
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GeneralServerResponse<>(
                    true,
                    e.getMessage(),
                    400,
                    null
            ));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<GeneralServerResponse<Token>> signin(@RequestBody SigninRequest dto) {
        try {
            Token token = authService.authenticate(dto);
            GeneralServerResponse<Token> response = new GeneralServerResponse<Token>(
                    false,
                    "Successfully authenticated user",
                    200,
                    token
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GeneralServerResponse<>(
                    true,
                    e.getMessage(),
                    400,
                    null
            ));
        }
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<GeneralServerResponse<VerifyViaEmail>> verifyEmail(@Valid @RequestBody VerifyViaEmailRequest dto) {
        try {
            VerifyViaEmail verification = authService.verifyEmail(dto);
            return ResponseEntity.ok(
                    new GeneralServerResponse<>(
                            false,
                            "Successfully verified email",
                            200,
                            verification
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GeneralServerResponse<>(
                    true,
                    e.getMessage(),
                    400,
                    null
            ));
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<GeneralServerResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest dto) {
        try {
            authService.resetPassword(dto);
            return ResponseEntity.ok(
                    new GeneralServerResponse<>(
                            false,
                            "Successfully reset password",
                            200,
                            null
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new GeneralServerResponse<>(
                    true,
                    e.getMessage(),
                    400,
                    null
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String entity) {
        return ResponseEntity.ok("logout");
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestBody String entity) {
        return ResponseEntity.ok("refreshToken");
    }


}
