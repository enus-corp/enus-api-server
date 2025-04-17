package com.enus.newsletter.model.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExchangeTokenRequest {
    @NotBlank
    private String tempToken;
}
