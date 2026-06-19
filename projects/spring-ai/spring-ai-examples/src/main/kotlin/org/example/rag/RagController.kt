package org.example.rag

import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RagController(private val ragService: RagService) {

    @GetMapping("/ai/rag")
    fun generate(
        @RequestParam(value = "message", defaultValue = "What bike is good for city commuting?") message: String
    ): AssistantMessage {
        return ragService.retrieve(message)
    }

}