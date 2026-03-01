package com.modulr.ollama.embed.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IngestionService {

    private final PgVectorStore pgVectorStore;
    Logger logger = Logger.getLogger(IngestionService.class.getName());
    @Value("classpath:/files/<your-pdf-file-name>.pdf")
    private Resource pdfResource;

    @Value("classpath:/files/<your-json-file-name>.json")
    private Resource jsonResource;

    public IngestionService(PgVectorStore pgVectorStore) {
        this.pgVectorStore = pgVectorStore;
    }

    public void loadPdf() {
        var pdfReader = new PagePdfDocumentReader(pdfResource);
        TextSplitter textSplitter = new TokenTextSplitter();
        pgVectorStore.accept(textSplitter.apply(pdfReader.get()));
        logger.info("Pdf Ingestion completed");
    }

    public void loadJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Document> docs = new ArrayList<>();
        JsonNode root;
        try {
            root = objectMapper.readTree(jsonResource.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode paths = root.path("paths");

        paths.fieldNames().forEachRemaining(path -> {
            JsonNode pathNode = paths.get(path);

            String content = "PATH: " + path + "\n" + pathNode.toString();

            docs.add(new Document(
                content,
                Map.of("type", "path", "path", path)
            ));
        });

        pgVectorStore.accept(docs);

        logger.info("Json Ingestion completed");
    }
}
