package com.enus.newsletter.model.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyViaEmailRequest {
    @NotBlank(message = "Email is required")
    @JsonProperty("email")
    @Email
    private String email;
}
