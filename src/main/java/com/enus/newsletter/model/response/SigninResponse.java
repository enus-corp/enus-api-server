package com.enus.newsletter.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SigninResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private List<String> roles;
}
