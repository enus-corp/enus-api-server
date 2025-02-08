package com.enus.newsletter.model.request;

import lombok.Data;

@Data
public class SaveKeywordEntity {
    private Long userId;
    private String[] keywords;
}
