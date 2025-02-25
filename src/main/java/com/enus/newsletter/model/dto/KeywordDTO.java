package com.enus.newsletter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordDTO {
    private Long id;
    private String word;
    private boolean notificationEnabled;
}
