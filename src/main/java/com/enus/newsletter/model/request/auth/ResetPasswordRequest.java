package com.enus.newsletter.model.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Email required")
    @NotBlank(message = "New Password required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?=.*[a-z]).{8,}$",
            message = "Password must have at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @JsonProperty("newPassword")
    private String newPassword;

    @NotBlank(message = "verification code required")
    @NotBlank(message = "Please check your email for the verification code")
    @JsonProperty("verificationCode")
    private String verificationCode;

    @NotBlank(message = "email code required")
    @JsonProperty("emailCode")
    private String emailCode;

}
