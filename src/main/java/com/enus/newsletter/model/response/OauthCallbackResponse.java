package com.enus.newsletter.model.response;

import com.enus.newsletter.model.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OauthCallbackResponse {
    @JsonProperty("token")
    private Token token;

    @JsonProperty("user")
    private UserDTO user;
}
