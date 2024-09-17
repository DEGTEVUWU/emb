package com.ivan_degtev.emb.config;

import com.ivan_degtev.emb.service.RAGAssistant;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

import static java.util.Arrays.asList;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AiConfig {

    @Value("${openai.token}")
    private String openAiToken;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiToken)
                .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
                .logRequests(true)
                .logRequests(true)
                .build();
    }

    @Bean
    @Lazy
    public  EmbeddingStoreIngestor createEmbeddingStoreIngestor() throws IOException {
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 20))
                .embeddingModel(embeddingModel())
                .embeddingStore(embeddingStore())
                .build();

        return embeddingStoreIngestor;
    }

    @Bean
    public RAGAssistant ragAssistant() {
        var contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore())
                .embeddingModel(embeddingModel())
                .maxResults(15)
                .build();

        var contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(asList("file_name", "index"))
                .build();

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .contentInjector(contentInjector)
                .build();

        return AiServices.builder(RAGAssistant.class)
                .chatLanguageModel(chatLanguageModel())
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(openAiToken)
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_LARGE)
                .build();
    }
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
}