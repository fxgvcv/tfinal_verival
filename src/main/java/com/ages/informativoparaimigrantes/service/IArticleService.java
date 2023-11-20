package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Article;

import java.util.List;

public interface IArticleService {
    Article save(Article article);

    List<Article> getArticles(Long articleId, String language);
}
