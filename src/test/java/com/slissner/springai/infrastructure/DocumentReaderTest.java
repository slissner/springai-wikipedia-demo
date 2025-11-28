package com.slissner.springai.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import com.slissner.springai.infrastructure.document.DocumentReader;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

class DocumentReaderTest {

    final DocumentReader documentReader = new DocumentReader();

  @Test
  void loadText() {
    final Resource stateFile = new ClassPathResource("/articles/Indera_SC.pdf");

      final List<Document> documents = documentReader.loadText(stateFile);

      assertNotNull(documents);
      assertEquals(1, documents.size());
      assertTrue(documents.getFirst().getText().contains("Indera"));
  }
}
