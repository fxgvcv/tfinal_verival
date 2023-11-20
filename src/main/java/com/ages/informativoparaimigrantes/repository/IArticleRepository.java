package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.Article;
import com.ages.informativoparaimigrantes.domain.ids.ArticleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IArticleRepository extends JpaRepository<Article, ArticleId> {

    // If parameter is null, skips where clause
    // If not, filters by id
    @Query(value = "SELECT article FROM Article article" +
            " WHERE (:articleId IS NULL OR article.articleId.id = :articleId)" +
            " AND (article.articleId.language = :language)")
    List<Article> getArticles(Long articleId, String language);
}
