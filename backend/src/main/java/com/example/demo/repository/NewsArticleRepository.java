package com.example.demo.repository;

import com.example.demo.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {

    Optional<NewsArticle> findByUrl(String url);

    // Optional: also check by title if some URLs are missing
    Optional<NewsArticle> findByTitle(String title);

    List<NewsArticle> findByUrlIn(List<String> urls);
}
