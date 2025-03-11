package com.enus.newsletter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsSummary {
    private String id;
    private String title;
    private String content;
    private List<String> sources;
    private String timestamp;
}
