package com.example.demo.service;

import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.modules.mediarss.types.MediaContent;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RssNewsService {

    // Example: multiple channel feeds
    private static final Map<String, String> SOURCES = Map.of(
            "bbc", "http://feeds.bbci.co.uk/news/rss.xml",
            "cnn", "http://rss.cnn.com/rss/edition.rss",
            "ndtv", "https://feeds.feedburner.com/ndtvnews-top-stories",
            "toi", "https://timesofindia.indiatimes.com/rssfeedstopstories.cms"
    );

    public List<Map<String, String>> getTopNews(String sourceKey) {
        List<Map<String, String>> articles = new ArrayList<>();
        try {
            String rssUrl = SOURCES.getOrDefault(sourceKey, SOURCES.get("bbc"));
            URL url = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            for (SyndEntry entry : feed.getEntries()) {
                articles.add(Map.of(
                        "title", entry.getTitle(),
                        "link", entry.getLink(),
                        "description", entry.getDescription() != null ? entry.getDescription().getValue() : "",
                        "publishedDate", entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : ""
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }
    public List<Map<String, String>> getMergedTopNews() {
        List<Map<String, String>> allArticles = new ArrayList<>();

        for (Map.Entry<String, String> entry : SOURCES.entrySet()) {
            try {
                URL url = new URL(entry.getValue());
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));

                for (SyndEntry item : feed.getEntries()) {
                    Map<String, String> article = new HashMap<>();
                    article.put("source", entry.getKey().toUpperCase());
                    article.put("title", item.getTitle());
                    article.put("link", item.getLink());
                    article.put("description", item.getDescription() != null ? item.getDescription().getValue() : "");
                    article.put("publishedDate", item.getPublishedDate() != null ? item.getPublishedDate().toString() : "");
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

// Inside your article mapping loop:
                    if (item.getPublishedDate() != null) {
                        article.put("publishedAt", isoFormat.format(item.getPublishedDate()));
                    } else {
                        article.put("publishedAt", ""); // empty if not available
                    }
                    // ✅ Try to extract image URL
                    String imageUrl = extractImageUrl(item);
                    article.put("imageUrl", imageUrl);

                    allArticles.add(article);
                }
            } catch (Exception e) {
                System.err.println("❌ Error fetching " + entry.getKey() + ": " + e.getMessage());
            }
        }

        return allArticles;
    }

    private String extractImageUrl(SyndEntry item) {
        try {
            // 1️⃣ Check MediaModule
            MediaEntryModule mediaModule = (MediaEntryModule) item.getModule(MediaEntryModule.URI);
            if (mediaModule != null && mediaModule.getMediaContents() != null) {
                for (MediaContent media : mediaModule.getMediaContents()) {
                    if (media.getReference() != null && media.getReference().toString().startsWith("http")) {
                        return media.getReference().toString();
                    }
                }
            }

            // 2️⃣ Check enclosure
            if (item.getEnclosures() != null && !item.getEnclosures().isEmpty()) {
                SyndEnclosure enclosure = item.getEnclosures().get(0);
                if (enclosure.getType() != null && enclosure.getType().startsWith("image")) {
                    return enclosure.getUrl();
                }
            }

            // 3️⃣ Parse from description <img src="">
            if (item.getDescription() != null) {
                String html = item.getDescription().getValue();
                Matcher matcher = Pattern.compile("<img[^>]+src=\"([^\"]+)\"").matcher(html);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (Exception ignored) {}

        return null; // no image found
    }

}
