package com.enus.newsletter.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotNull
    @NotBlank(message = "New Password required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?=.*[a-z]).{8,}$",
            message = "Password must have at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String newPassword;

    @NotNull
    @NotBlank(message = "Please check your email for the verification code")
    private String verificationCode;

    @NotNull
    private String emailCode;

}
