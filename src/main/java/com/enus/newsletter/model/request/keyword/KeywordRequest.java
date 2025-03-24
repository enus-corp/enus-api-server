package com.enus.newsletter.model.request.keyword;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KeywordRequest {
    @NotBlank(message = "Keyword is mandatory")
    private String word;
    @JsonProperty("notificationEnabled")
    private boolean notificationEnabled = true;
}
