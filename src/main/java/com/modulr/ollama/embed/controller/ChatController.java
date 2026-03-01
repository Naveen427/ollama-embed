package com.modulr.ollama.embed.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modulr.ollama.embed.service.IngestionService;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final IngestionService ingestionService;

    public ChatController(ChatClient.Builder chatClientBuilder, PgVectorStore pgVectorStore,
                          IngestionService ingestionService) {
        this.ingestionService = ingestionService;
        this.chatClient = chatClientBuilder
            .defaultAdvisors(QuestionAnswerAdvisor.builder(pgVectorStore).build())
            .build();
    }


    @PostMapping("/load-pdf")
    public void loadPdf() {
        ingestionService.loadPdf();
    }

    @PostMapping("/load-json")
    public void loadJson() {
        ingestionService.loadJson();
    }

    @GetMapping("questions")
    public String question(@RequestParam String question) {
        return chatClient.prompt()
            .user(question)
            .call().content();
    }
}