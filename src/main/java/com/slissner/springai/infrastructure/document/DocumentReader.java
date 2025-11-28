package com.slissner.springai.infrastructure.document;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DocumentReader {
  public List<Document> loadText(final Resource resource) {
    final TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
    return tikaDocumentReader.read();
  }
}
