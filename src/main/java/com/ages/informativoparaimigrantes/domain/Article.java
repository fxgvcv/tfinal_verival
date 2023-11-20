package com.ages.informativoparaimigrantes.domain;

import com.ages.informativoparaimigrantes.domain.ids.ArticleId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Article {

    @EmbeddedId
    @JsonUnwrapped
    private ArticleId articleId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = true)
    private String externalUrl;


    public Article(ArticleId articleId, String title, String content){
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.externalUrl = null;
    }
}
