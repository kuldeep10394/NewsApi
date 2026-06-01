package com.example.demo.controller;

import com.example.demo.service.RssNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rss")
@CrossOrigin(origins = "http://localhost:5173")
public class RssNewsController {

    private final RssNewsService rssNewsService;

    public RssNewsController(RssNewsService rssNewsService) {
        this.rssNewsService = rssNewsService;
    }

    @GetMapping("/top")
    public List<Map<String, String>> getNews(@RequestParam(required = false) String source) {
        return rssNewsService.getTopNews(source);
    }
    @GetMapping("/all")
    public List<Map<String, String>> getAllNews() {
        return rssNewsService.getMergedTopNews();
    }
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, String>>> searchNews(@RequestParam String keyword) {
        List<Map<String, String>> allArticles = rssNewsService.getMergedTopNews();

        // ✅ Filter by keyword (case-insensitive)
        List<Map<String, String>> filtered = allArticles.stream()
                .filter(article -> {
                    String title = article.getOrDefault("title", "").toLowerCase();
                    String desc = article.getOrDefault("description", "").toLowerCase();
                    return title.contains(keyword.toLowerCase()) || desc.contains(keyword.toLowerCase());
                })
                .toList();

        return ResponseEntity.ok(filtered);
    }
}
