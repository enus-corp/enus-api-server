package com.enus.newsletter.model.request.keyword;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is mandatory")
    @JsonProperty("refreshToken")
    private String refreshToken;
}
