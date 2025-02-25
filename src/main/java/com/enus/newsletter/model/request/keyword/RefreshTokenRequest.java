package com.enus.newsletter.model.request.keyword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotNull
    @NotBlank(message = "Refresh token is mandatory")
    private String refreshToken;
}
