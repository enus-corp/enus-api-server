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

import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.tts.TtsRequest;
import com.enus.newsletter.service.TtsService;

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
    public ResponseEntity<?> setTTS(@AuthenticationPrincipal UserDTO currentUser, @Valid @RequestBody TtsRequest dto) {
        log.info("Current user: {}", currentUser);
        log.info("Received request to set TTS speed: {}", dto);
        // service.saveTTSConfig(dto);
        return ResponseEntity.ok().build();
    }
}
