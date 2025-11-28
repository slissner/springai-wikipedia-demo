package com.slissner.springai.application;

import com.slissner.springai.infrastructure.repository.ArticleRepository;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

  private final ChatClient chatClient;
  private final ArticleRepository articleRepository;

  public ArticleService(
      final ArticleRepository articleRepository, final ChatClient.Builder chatClientBuilder) {
    this.articleRepository = articleRepository;
    this.chatClient = chatClientBuilder.build();
  }

  public String summarizeArticles() {
    final List<Document> articles = articleRepository.getAll();

    // Use the first article as an example
    final Document articleContent = articles.getFirst();

    return chatClient
        .prompt()
        .user("Provide a summary of the following article:\n\n" + articleContent)
        .call()
        .content();
  }
}
