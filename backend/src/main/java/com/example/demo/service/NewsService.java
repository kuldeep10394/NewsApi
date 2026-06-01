package com.example.demo.service;


import com.example.demo.repository.NewsArticleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class NewsService {

    @Value("${news.api.key}")
    private String  apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final NewsArticleRepository newsArticleRepository;

    public NewsService(NewsArticleRepository newsArticleRepository) {
        this.newsArticleRepository = newsArticleRepository;
    }

    public Map<String, Object> getTopHeadlines(String category,String query) {
        String baseUrl;
        Map<String, Object> response;

        // ✅ Case 1: Keyword search (everything endpoint)
        if (query != null && !query.isBlank()) {
            baseUrl = UriComponentsBuilder
                    .fromHttpUrl("https://newsapi.org/v2/everything")
                    .queryParam("q", query)
                    .queryParam("language", "en")
                    .queryParam("sortBy", "publishedAt")
                    .queryParam("pageSize", 20)
                    .queryParam("apiKey", apiKey)
                    .toUriString();
        }
        // ✅ Case 2: Top headlines (category & country)
        else {
            baseUrl = UriComponentsBuilder
                    .fromHttpUrl("https://newsapi.org/v2/top-headlines")
                    .queryParam("country", "us")
                    .queryParamIfPresent("category", Optional.ofNullable(category))
                    .queryParam("pageSize", 20)
                    .queryParam("apiKey", apiKey)
                    .toUriString();
        }

        System.out.println("🔗 Fetching: " + baseUrl);

        response = restTemplate.getForObject(baseUrl, Map.class);

        if (response == null || response.get("articles") == null) return response;

        /*List<Map<String, Object>> articles = (List<Map<String, Object>>) response.get("articles");

        // ✅ 1️⃣ Extract all URLs from the fetched articles
        List<String> urls = articles.stream()
                .map(a -> (String) a.get("url"))
                .filter(Objects::nonNull)
                .toList();

        // ✅ 2️⃣ Fetch all existing URLs from DB in one query
        List<String> existingUrls = newsArticleRepository.findByUrlIn(urls)
                .stream()
                .map(NewsArticle::getUrl)
                .collect(Collectors.toList());

        Set<String> existingUrlSet = new HashSet<>(existingUrls);



        // ✅ 3️⃣ Prepare only NEW articles
        List<NewsArticle> newArticles = articles.stream()
                .filter(a -> a.get("url") != null && !existingUrlSet.contains(a.get("url")))
                .map(a -> {
                    NewsArticle article = new NewsArticle();
                    Map<String, Object> sourceMap = (Map<String, Object>) a.get("source");
                    article.setSource(sourceMap != null ? (String) sourceMap.get("name") : null);
                    article.setAuthor((String) a.get("author"));
                    article.setTitle((String) a.get("title"));
                    article.setDescription((String) a.get("description"));
                    article.setUrl((String) a.get("url"));
                    article.setUrlToImage((String) a.get("urlToImage"));
                    article.setPublishedAt((String) a.get("publishedAt"));
                    article.setContent((String) a.get("content"));
                    article.setCategory(category);
                    article.setCountry("US");
                    return article;
                })
                .collect(Collectors.toList());

        // ✅ 4️⃣ Batch insert only new ones (1 DB hit)
        if (!newArticles.isEmpty()) {
            newsArticleRepository.saveAll(newArticles);
            System.out.println("🟢 Saved " + newArticles.size() + " new articles.");
        } else {
            System.out.println("ℹ️ No new articles found.");
        }*/

        return response;
    }
}
