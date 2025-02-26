package com.enus.newsletter.model.request.keyword;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateKeywordNotificationRequest {
    @NotNull
    private long id;

    @NotNull
    private boolean notificationEnabled;
}
