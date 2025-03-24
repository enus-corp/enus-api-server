/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.service;

import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.TtsEntity;
import com.enus.newsletter.db.repository.TtsRepository;
import com.enus.newsletter.enums.DetailLevel;
import com.enus.newsletter.enums.ReadSpeed;
import com.enus.newsletter.model.dto.TtsDetails;
import com.enus.newsletter.model.dto.TtsDto;
import com.enus.newsletter.model.request.tts.TtsRequest;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idohyeon
 */
@Slf4j(topic = "TTS_SERVICE")
@Service
public class TtsService {

    private final TtsRepository repository;

    public TtsService(TtsRepository repository) {
        this.repository = repository;
    }

    public TtsDto saveTTSConfig(TtsRequest dto) {
        log.info("Saving TTS config: {}", dto);
        TtsEntity entity = repository.save(dto);

        int wordsPerBriefing = DetailLevel.getValueByName(dto.getDetailLevel());
        int estimatedSeconds = (int) ((wordsPerBriefing * entity.getBriefingCount()) / (5 * ReadSpeed.getValueByName(dto.getReadSpeed())));

        TtsDetails details = TtsDetails.builder()
            .estimatedSeconds(estimatedSeconds)
            .wordsPerBriefing(wordsPerBriefing)
            .wordCount(wordsPerBriefing * entity.getBriefingCount())
            .build();

        TtsDto ttsDto = TtsDto.builder()
            .chatId(entity.getChatSession().getChatId())
            .readSpeed(ReadSpeed.getValueByName(dto.getReadSpeed()))
            .detailLevel(DetailLevel.getValueByName(dto.getDetailLevel()))
            .briefingCount(entity.getBriefingCount())
            .ttsDetails(details)
            .build();
        return ttsDto;
    }
}
