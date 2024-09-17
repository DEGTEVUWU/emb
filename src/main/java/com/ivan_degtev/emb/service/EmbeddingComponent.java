package com.ivan_degtev.emb.service;


import com.ivan_degtev.emb.util.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Component
@AllArgsConstructor
@Slf4j
public class EmbeddingComponent {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;
    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    // Указываем директорию для поиска файлов
    private static final String FILES_DIRECTORY = "src/main/resources/files";



    public void loadEachDocument(String fileName) {
        String fullPathToFile = FILES_DIRECTORY + "/" + fileName;
        log.info("ПАолный путь к файлу {}", fullPathToFile);

        DocumentParser parser = new ApacheTikaDocumentParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/" + fileName);

        Document document = parser.parse(inputStream);

        embeddingStoreIngestor.ingest(document);
    }

    public void findAndLoadAllDocuments() {
        Path dirPath = Paths.get(FILES_DIRECTORY);

        try (Stream<Path> filePathStream = Files.list(dirPath)) {
            List<String> filesName = filePathStream
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());

            for (String fileName : filesName) {
                loadEachDocument(fileName);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файлов из директории: " + e.getMessage());
        }
    }
}
