package com.example.demo.service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String getNewsFromChatGPT(String query) {
        OpenAiService service = new OpenAiService(apiKey);

        String prompt = """
            You are a news assistant. Provide the latest updates and trending topics about the query below.
            Format your response with clear bullet points and include short summaries.
            Query: %s
            """.formatted(query);

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .prompt(prompt)
                .maxTokens(400)
                .temperature(0.7)
                .build();

        return service.createCompletion(request)
                .getChoices()
                .get(0)
                .getText()
                .trim();
    }
}
