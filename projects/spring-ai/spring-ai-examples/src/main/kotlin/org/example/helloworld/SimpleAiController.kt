package org.example.helloworld

import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SimpleAiController(private val chatClient: ChatClient) {

    @GetMapping("/ai/simple")
    fun generation(
        @RequestParam(value = "message", defaultValue = "Tell me a joke") message: String
    ): Map<String, String> {
        return mapOf("generation" to chatClient.prompt().user(message).call().content()!!)
    }
}