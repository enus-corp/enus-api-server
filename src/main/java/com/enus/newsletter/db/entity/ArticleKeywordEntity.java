//package com.enus.newsletter.db.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name="article_keywords")
//@Data
//public class ArticleKeywordEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "article_id")
//    private ArticleEntity article;
//
//    @ManyToOne
//    @JoinColumn(name = "keyword_id")
//    private KeywordEntity keyword;
//
//    @Column(nullable = false)
//    private Double relevanceScore;
//}
