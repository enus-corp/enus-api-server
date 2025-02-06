package com.enus.newsletter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.service.KeywordService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Tag(name="Keyword", description = "Keyword")
@RestController
@RequestMapping("/api/keyword")
@Slf4j(topic = "KEYWORD_CONTROLLER")
public class KeywordController {
    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping("/{userId}/keywords")
    public ResponseEntity<GeneralServerResponse<Void>> getMethodName(@PathVariable("userId") Long userId) {
        keywordService.getUserKeywords(userId);
        return ResponseEntity.ok(new GeneralServerResponse<>(
            false, 
            "Successfully returned registerd keywords", 
            0, 
            null));
    }
    
    
}
