//package com.ages.informativoparaimigrantes.controller;
//
//import com.ages.informativoparaimigrantes.domain.Article;
//import com.ages.informativoparaimigrantes.domain.ids.ArticleId;
//import com.ages.informativoparaimigrantes.service.ArticleServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(ArticleController.class)
//
//public class ArticleControllerTest
//{
//    private String baseEndpoint = "/articles";
//
//    @MockBean
//    private ArticleServiceImpl service;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//     @Test
//     public void getArticlesWithoutIdShouldReturnAllArticlesByLanguage() throws Exception {
//         List<Article> mockArticles = List.of(
//                 new Article(new ArticleId(1L, "pt" ), "title1", "content1"),
//                 new Article(new ArticleId(2L, "pt"), "title2" , "content2"),
//                 new Article(new ArticleId(3L, "pt" ), "title3", "content3"));
//         when(service.getArticles(eq(null), eq("pt"))).thenReturn(mockArticles);
//
//         mockMvc.perform(get(baseEndpoint + "?language=pt"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("[{\"id\":1,\"language\":\"pt\",\"title\":\"title1\",\"content\":\"content1\"},{\"id\":2,\"language\":\"pt\",\"title\":\"title2\",\"content\":\"content2\"},{\"id\":3,\"language\":\"pt\",\"title\":\"title3\",\"content\":\"content3\"}]"));
//     }
//
//    @Test
//    public void getArticlesWithIdAndLanguageShouldReturn1Article() throws Exception {
//        List<Article> mockArticles = List.of(
//                new Article(new ArticleId(1L, "pt" ), "title1", "content1"));
//        when(service.getArticles(eq(1L), eq("pt"))).thenReturn(mockArticles);
//
//        mockMvc.perform(get(baseEndpoint+"/1?language=pt"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"id\":1,\"language\":\"pt\",\"title\":\"title1\",\"content\":\"content1\"}"));
//    }
//
//    @Test
//    public void getArticlesWithStringShouldReturnBadRequest() throws Exception {
//        mockMvc.perform(get(baseEndpoint+"/lucas"))
//                .andExpect(status().isBadRequest());
//    }
//
//   /* @Test
//    public void saveSuccessufullySavesEntityBadRequest() throws Exception {
//        Article article= new Article(new ArticleId(1L, "pt" ), "title1", "content1");
//        String jsontest = new ObjectMapper().writeValueAsString(article);
//        when(service.getArticles(eq(1L), eq("pt"))).thenReturn(List.of(article));
//
//        mockMvc.perform(post(baseEndpoint+"/savearticle").contentType(MediaType.TEXT_PLAIN)
//                .content(jsontest)).andExpect(status().isBadRequest());
//     }
//
//    */
//
//}


