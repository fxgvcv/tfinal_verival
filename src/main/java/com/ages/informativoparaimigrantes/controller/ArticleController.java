package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Article;
import com.ages.informativoparaimigrantes.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @GetMapping("/{articleId}")
    public ResponseEntity<Article> getArticle(@PathVariable Long articleId, @RequestParam(required = true) String language)
    {
        List<Article> articles = articleServiceImpl.getArticles(articleId, language);

        if (!articles.isEmpty())
        {
            return new ResponseEntity<>(articles.get(0), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles(@RequestParam(required = true) String language)
    {
        List<Article> articles = articleServiceImpl.getArticles(null, language);

        // Does not return 404 because lists are expected to be empty if no entities were found.
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> saveArticle(@RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleServiceImpl.save(article));
    }
}
