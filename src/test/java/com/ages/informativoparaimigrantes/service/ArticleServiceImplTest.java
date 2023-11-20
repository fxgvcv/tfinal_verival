package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Article;
import com.ages.informativoparaimigrantes.domain.ids.ArticleId;
import com.ages.informativoparaimigrantes.repository.IArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    private IArticleRepository repository = mock(IArticleRepository.class);

    private final ArticleServiceImpl service = new ArticleServiceImpl(repository);

    private final List<Article> mockArticles = List.of(
            new Article(new ArticleId(1L, "pt" ), "title1", "content1"),
            new Article(new ArticleId(2L, "pt"), "title2" , "content2"),
            new Article(new ArticleId(3L, "pt" ), "title3", "content3"));


    @Test
    public void getArticlesWithoutIdShouldReturnAllArticlesByLanguage() {
        when(repository.getArticles(eq(null),eq("pt"))).thenReturn(mockArticles);

        List<Article> actual = service.getArticles(null, "pt");

        assertIterableEquals(actual, mockArticles);
    }

    @Test
    public void getArticlesWithAppNumberShouldReturn1Article() {
        List<Article> mockArticles = List.of(new Article(new ArticleId(1L, "pt" ), "title1", "content1"));
        when(repository.getArticles(eq(1L),eq("pt"))).thenReturn(mockArticles);
        List<Article> actual = service.getArticles(1L, "pt");

        assertIterableEquals(actual, mockArticles);
    }

    @Test
    public void saveSuccessufullySavesEntity(){
       Article article= new Article(new ArticleId(1L, "pt" ), "title1", "content1");
       when(repository.save(article)).thenReturn(article);
       Article articleresp = service.save(article);
       assertEquals(article,articleresp);
    }
}
