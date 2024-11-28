package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Article;
import com.ages.informativoparaimigrantes.domain.Program;
import com.ages.informativoparaimigrantes.domain.UserData;
import com.ages.informativoparaimigrantes.domain.ids.ArticleId;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.repository.IArticleRepository;
import com.ages.informativoparaimigrantes.repository.IProgramRepository;
import com.ages.informativoparaimigrantes.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {

    private final IArticleRepository repository;

    @Autowired
    public ArticleServiceImpl(IArticleRepository repository) {
        this.repository = repository;
    }

    public List<Article> getArticles(Long articleId, String language){
        return repository.getArticles(articleId, language);
    }

    public Article save(Article article){
        return repository.save(article);
    }
}
