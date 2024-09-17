package com.ivan_degtev.emb.controller;


import com.ivan_degtev.emb.service.EmbeddingComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoadController {

    private final EmbeddingComponent embeddingComponent;

    @GetMapping("/load")
    public void loadSingle() {
        log.info("Loading all documents start");
        embeddingComponent.findAndLoadAllDocuments();
        log.info("Loading all documents end");
    }
}
