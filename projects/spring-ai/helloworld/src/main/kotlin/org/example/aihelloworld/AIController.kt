package org.example.aihelloworld

import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AIController(private val chatClient: ChatClient) {

    @GetMapping("/ai")
    fun completion(
        @RequestParam(value = "message", defaultValue = "Tell me a joke") message: String
    ): Map<String, String> {
        return mapOf(
            "completion" to
                    chatClient.prompt()
                        .user(message)
                        .call()
                        .content()!!
        )
    }
}