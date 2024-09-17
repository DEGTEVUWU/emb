package com.ivan_degtev.emb.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScannerService {

    private final RAGAssistant ragAssistant;

    public void scanner() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String question = scanner.nextLine();

            if (question.equals("exit")) {
                log.info("тестовый сканер закрыт!");
                break;
            }

            String answer = ragAssistant.chat("12", question);
            log.info("Ответ от тест чата, сканер: {}", answer);
        }
        log.info("Сканнер закрыт!");
    }
}
