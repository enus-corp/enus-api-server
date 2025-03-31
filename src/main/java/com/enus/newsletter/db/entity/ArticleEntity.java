package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "article_id", unique = true)
    private String articleId;

    @Column(nullable = false, name = "media_code")
    private String mediaCode;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, name = "section_code")
    private String sectionCode;

    @Column(nullable = false, name = "subsection_code")
    private String subsectionCode;

    @Column(nullable = false, name = "publish_date")
    private LocalDateTime publishDate;
}
