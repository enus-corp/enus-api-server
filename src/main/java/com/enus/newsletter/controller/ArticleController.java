package com.enus.newsletter.controller;

import com.enus.newsletter.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Article", description = "Article API")
@Slf4j(topic = "ARTICLE_CONTROLLER")
@RestController
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

}
