package com.slissner.springai.infrastructure.repository;

import com.slissner.springai.infrastructure.document.DocumentReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository {

  private final DocumentReader documentReader;

  public ArticleRepository(final DocumentReader documentReader) {
    this.documentReader = documentReader;
  }

  private static final List<String> DOCUMENT_PATHS =
      Stream.of(
              "2023_Asia_Contents_Awards_&_Global_OTT_Awards.pdf",
              "Anthony_Wonke.pdf",
              "Chang_Tzi-chin.pdf",
              "Indera_SC.pdf",
              "Neant-sur-Yvel.pdf")
          .map(path -> "/articles/" + path)
          .toList();

  public List<Document> getAll() {
    return DOCUMENT_PATHS.stream()
        .map(ClassPathResource::new)
        .map(documentReader::loadText)
        .flatMap(Collection::stream)
        .toList();
  }
}
