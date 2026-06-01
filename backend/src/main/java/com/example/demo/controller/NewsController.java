package com.example.demo.controller;

import com.example.demo.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/top")
    public ResponseEntity<Map<String, Object>> getTopNews(@RequestParam(required = false) String category, @RequestParam(required = false) String query) {
        Map<String, Object> response = newsService.getTopHeadlines(category,query);
        return ResponseEntity.ok(response);
    }
}