package com.enus.newsletter.model.request.keyword;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateKeywordNotificationRequest {
    @NotNull
    @JsonProperty("id")
    private long id;

    @NotNull
    @JsonProperty("notificationEnabled")
    private boolean notificationEnabled;
}
