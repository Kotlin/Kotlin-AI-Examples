package org.example.langchain4j.aiservice

import org.springframework.web.bind.annotation.RestController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * This is an example of using an [dev.langchain4j.service.spring.AiService], a high-level LangChain4j API
 */
@RestController
class AssistantController(
    private val assistant: Assistant,
    private val streamingAssistant: StreamingAssistant
) {

    @GetMapping("/assistant")
    fun assistant(@RequestParam(value = "message", defaultValue = "What is the time now?") message: String): String {
        return assistant.chat(message)
    }

    @GetMapping("/streamingAssistant")
    fun streamingAssistant(
        @RequestParam(value = "message", defaultValue = "What is the time now?") message: String
    ): Flow<String> {
        return streamingAssistant.chat(message).asFlow()
    }
}