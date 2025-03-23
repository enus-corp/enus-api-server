/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.model.request.user;

import com.enus.newsletter.enums.DetailLevel;
import com.enus.newsletter.enums.ReadSpeed;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author idohyeon
 */
@Data
public class TtsDto {
    @NotNull
    private ReadSpeed readSpeed;
    
    @NotNull
    private DetailLevel detailLevel;

    @Min(5)
    @Max(30)
    private int briefingCount = 10;

}
