package com.slissner.springai;

import com.slissner.springai.application.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiApplication {

  private static final Logger log = LoggerFactory.getLogger(SpringAiApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(SpringAiApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(final ArticleService articleService) {
    return args -> {
      log.info("Okay, I am going to summarize articles...");

      final String summary = articleService.summarizeArticles();

      log.info("The summary is:");
      log.info(summary);
      log.info("Done!");
    };
  }
}
