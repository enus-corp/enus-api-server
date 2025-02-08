package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PopularArticle")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PopularArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "article_id")
    private String articleId;

    @Column(nullable = false, name = "media_code")
    private String mediaCode;

    @Column(nullable = false, name = "media_name")
    private String media_name;
}
