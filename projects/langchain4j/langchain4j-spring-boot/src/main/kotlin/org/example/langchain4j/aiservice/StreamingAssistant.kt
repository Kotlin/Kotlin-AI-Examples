package org.example.langchain4j.aiservice

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.spring.AiService
import reactor.core.publisher.Flux

@AiService
interface StreamingAssistant {

    @SystemMessage("You are a polite assistant")
    fun chat(userMessage: String): Flux<String>
}