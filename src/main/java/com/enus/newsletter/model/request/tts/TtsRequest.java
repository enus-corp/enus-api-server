/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.model.request.tts;

import com.enus.newsletter.enums.DetailLevel;
import com.enus.newsletter.enums.ReadSpeed;
import com.enus.newsletter.validation.ValidEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author idohyeon
 */
@Data
public class TtsRequest {
    @NotBlank(message = "chatId is required")
    @JsonProperty("chatId")
    private String chatId;
    
    @NotBlank(message = "readSpeed is required")
    @ValidEnum(enumClass=ReadSpeed.class)
    @JsonProperty("readSpeed")
    private String readSpeed;
    
    @NotBlank(message = "detailLevel is required")
    @ValidEnum(enumClass=DetailLevel.class)
    @JsonProperty("detailLevel")
    private String detailLevel;

    @Min(5)
    @Max(30)
    @JsonProperty("briefingCount")
    private int briefingCount = 10;

}
