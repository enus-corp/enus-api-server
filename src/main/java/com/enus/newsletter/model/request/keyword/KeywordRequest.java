package com.enus.newsletter.model.request.keyword;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KeywordRequest {
    @NotBlank(message = "Keyword is mandatory")
    private String word;
    private boolean notificationEnabled = true;
}
