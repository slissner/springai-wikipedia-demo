package com.slissner.springai.application;

import com.slissner.springai.infrastructure.repository.ArticleRepository;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

  private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
  private static final int MAX_LENGTH = 8000;

  private final ArticleRepository articleRepository;
  private final ChatClient chatClient;
  private final ChatMemory chatMemory;

  public ArticleService(
      final ArticleRepository articleRepository,
      final ChatClient.Builder chatClientBuilder,
      final ChatMemory chatMemory) {
    this.articleRepository = articleRepository;
    this.chatClient = chatClientBuilder.build();
    this.chatMemory = chatMemory;
  }

  public String summarizeArticles() {
    final MessageChatMemoryAdvisor chatMemoryAdvisor =
        MessageChatMemoryAdvisor.builder(chatMemory).build();

    final List<Document> articles = articleRepository.getAll();

    articles.stream()
        .filter(article -> StringUtils.isNotBlank(article.getText()))
        // Max length of 8000 characters to avoid API limits
        .map(abbreviateArticleContent())
        .forEach(
            articleContent -> {
              try {
                log.info("Calling AI API with article. [id={}]", articleContent.id());

                chatClient
                    .prompt()
                    .advisors(chatMemoryAdvisor)
                    .user("Provide a summary of the following article:\n\n" + articleContent.text())
                    .call()
                    // We need to call .content() in order to actually retrieve the answer and store
                    // it
                    // in the chat memory.
                    .content();

                log.info(
                    "Successfully summarized article content with AI model. Sleeping now... [id={}]",
                    articleContent.id());

                // Sleep for 60 seconds after each API call to avoid rate limiting
                Thread.sleep(60000);

              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted during sleep", e);
              }
            });

    return chatClient
        .prompt()
        .advisors(chatMemoryAdvisor)
        .user(
            "Imagine you are a journalist in a news outlet. You work for a travel magazine that is based in Europe, "
                + "and thus its readers are European travelers. A colleague of yours has summarized five articles for "
                + "you. Now it is your turn to pick a subject out of these five articles and write a short travel "
                + "recommendation. The idea is that you write a single paragraph that praises a destination or activity "
                + "that you want to recommend to your readers."
                + "\n\n"
                + "Given the summaries provided, what is the most interesting topic?")
        .call()
        .content();
  }

  private static Function<Document, ArticleContent> abbreviateArticleContent() {
    return article -> {
      final String content = article.getText();
      if (content.length() > MAX_LENGTH) {
        return new ArticleContent(article.getId(), StringUtils.abbreviate(content, MAX_LENGTH));
      }
      return new ArticleContent(article.getId(), content);
    };
  }

  private record ArticleContent(String id, String text) {}
}
