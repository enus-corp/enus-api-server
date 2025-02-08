package com.enus.newsletter.controller;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.model.request.SaveKeywordEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.enus.newsletter.service.KeywordService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;


@Tag(name="Keyword", description = "Keyword")
@RestController
@RequestMapping("/api/keyword")
@Slf4j(topic = "KEYWORD_CONTROLLER")
public class KeywordController {
    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping("getKeywords/{userId}")
    public ResponseEntity<GeneralServerResponse<Void>> getKeywords(@PathVariable("userId") Long userId) {
        keywordService.getUserKeywords(userId);
        return ResponseEntity.ok(new GeneralServerResponse<>(
            false, 
            "Successfully returned registerd keywords", 
            0, 
            null));
    }

    @PostMapping("addKeywords")
    public ResponseEntity<GeneralServerResponse<Void>> saveKeywords(@Valid @RequestBody SaveKeywordEntity req) {
        keywordService.addKeyword(req);
        return ResponseEntity.ok(new GeneralServerResponse<>(
            false,
            "Successfully saved keywords",
            0,
            null));
    }
    
    
}
