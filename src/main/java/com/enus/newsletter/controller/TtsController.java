/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.db.entity.TtsEntity;
import com.enus.newsletter.model.dto.TtsDto;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.tts.TtsRequest;
import com.enus.newsletter.service.TtsService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idohyeon
 */
@Slf4j(topic = "TTS_CONTROLLER")
@Tag(name="Tts", description = "Chatroom TTS Config")
@RestController
@RequestMapping("/api/tts")
public class TtsController {

    private final TtsService service;

    TtsController(TtsService service) {
        this.service = service;
    }

    @PostMapping("/set-tts")
    public ResponseEntity<GeneralServerResponse<TtsDto>> setTTS(@AuthenticationPrincipal UserDTO currentUser, @Valid @RequestBody TtsRequest req) {
        log.info("Current user: {}", currentUser);
        log.info("Received request to set TTS speed: {}", req);
        TtsDto ttsDto = service.saveTTSConfig(req);
        GeneralServerResponse<TtsDto> response = new GeneralServerResponse<>(
            false,
            "Successfully saved TTS config for chatID: " + req.getChatId(),
            0,
            ttsDto
        );
        return ResponseEntity.ok(response);
    }
}
