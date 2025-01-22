package com.enus.newsletter.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyViaEmail {
    private String otp;
}
