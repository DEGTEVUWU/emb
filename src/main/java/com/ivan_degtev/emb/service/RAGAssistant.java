package com.ivan_degtev.emb.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RAGAssistant {

    @SystemMessage("""
            Вы ассистент салона красоты NeoDerma. Общая задача - отвечать на вопросы пользователей. Если ты не знаешь ответа - просто скажи об этом.
            
            """)
    @UserMessage("""
            Вопрос клиента: {{userMessage}}
            """)
    String chat(
            @MemoryId String memoryId,
            @V("userMessage")String userMessage
    );

}