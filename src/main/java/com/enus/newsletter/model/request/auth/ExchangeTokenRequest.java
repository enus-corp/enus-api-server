package com.enus.newsletter.model.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeTokenRequest {
    @NotBlank(message = "Temp Token is required")
    @JsonProperty("tempToken")
    private String tempToken;
}
