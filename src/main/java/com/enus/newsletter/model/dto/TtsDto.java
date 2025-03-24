package com.enus.newsletter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TtsDto {
    private String chatId;
    private double readSpeed;
    private int detailLevel;
    private int briefingCount;
    private TtsDetails ttsDetails;
}
