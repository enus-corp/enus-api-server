package com.enus.newsletter.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Token to JSON", e);
        }
    }

    public static Token fromJson(String json) {
        try {
            return objectMapper.readValue(json, Token.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to Token", e);
        }
    }
    
}
