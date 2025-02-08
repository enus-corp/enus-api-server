package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

@Entity
@Table(name="Keyword")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word;

    @OneToMany(mappedBy = "keyword")
    private Set<UserKeywordEntity> users;

//    @OneToMany(mappedBy = "keyword")
//    private Set<ArticleKeywordEntity> articles;

}
