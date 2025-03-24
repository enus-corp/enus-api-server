package com.enus.newsletter.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TtsDetails {
    private int estimatedSeconds;
    private int wordsPerBriefing;
    private int wordCount;
}
