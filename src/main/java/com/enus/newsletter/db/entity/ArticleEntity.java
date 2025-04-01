package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

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

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, name = "section_code")
    private String sectionCode;

    @Column(nullable = false, name = "subsection_code")
    private String subsectionCode;

    @Column(nullable = false, name = "publish_date")
    private LocalDateTime publishDate;

    @ManyToMany
    @JoinTable(
        name = "article_summarize_article", // name of join table
        joinColumns = @JoinColumn(name = "article_id"), // name of column in this table
        inverseJoinColumns = @JoinColumn(name = "summarized_article_id") // name of column in the join table
    )
    private Set<SummarizedArticleEntity> summarizedArticles;
}
