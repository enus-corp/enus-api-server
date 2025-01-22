package com.enus.newsletter.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    @JsonProperty("token")
    private String token;

    @JsonProperty("expires_in")
    private long expiresIn;
}
