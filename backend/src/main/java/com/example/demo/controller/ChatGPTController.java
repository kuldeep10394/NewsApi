package com.example.demo.controller;

import com.example.demo.service.ChatGPTService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatgpt")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @GetMapping("/news")
    public String getNews(@RequestParam String query) {
        return chatGPTService.getNewsFromChatGPT(query);
    }
}
