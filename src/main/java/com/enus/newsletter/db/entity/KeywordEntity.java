package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Set;

@Entity
@Table(name="keywords")
@Data
public class KeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word;

    @OneToMany(mappedBy = "keyword")
    private Set<UserKeywordEntity> users;

    @OneToMany(mappedBy = "keyword")
    private Set<ArticleKeywordEntity> articles;


}
