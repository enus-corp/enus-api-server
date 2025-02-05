//package com.enus.newsletter.db.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Entity
//@Table(name = "articles")
//@Data
//public class ArticleEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String content;
//
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String summary;
//
//    @Column(nullable = false)
//    private String sourceUrl;
//
//    @Column(nullable = false)
//    private LocalDateTime publishDate;
//
//    @Column(nullable = false)
//    private LocalDateTime crawledDate;
//
//    @OneToMany(mappedBy = "article")
//    private Set<ArticleKeywordEntity> keywords;
//}
